import io.ngin.pipeline.semver.Component

def call(Map parameters = [:]) {

  def problems = []
  String nextVersion = null

  def artifactory = getArtifactoryServer('ngin-artifactory')
  def rtMaven = newMavenBuild()
  def buildInfo = newBuildInfo()

  pipeline {
    agent any

    tools {
      maven 'maven3'
      jdk parameters.get('jdk', 'jdk8')
    }

    environment {
      GIT_AUTHOR = 'NGIN Jenkins <cicd@ngin.io>'
      UPSTREAM_REMOTE = parameters.get('remoteName', 'origin')
      UPSTREAM_BRANCH = parameters.get('trunkBranch', 'master')

      EXISTING_VERSION = mavenGetVersion()
      SEMVER_INCREMENT = semverFindTag()
      // can't use this as an enviroment variable
      // https://issues.jenkins-ci.org/browse/JENKINS-50269
      // MERGE_REQUESTED = readyForMerge()
    }

    stages {
      stage('Analyze') {
        steps {
          script {
            // https://github.com/cloudbees/groovy-cps/issues/89
            boolean ff = gitCanFastForward()
            echo "ff: $ff"
            unstableUnless(ff, "unable to fast-forward this branch onto $UPSTREAM_BRANCH".toString(), problems)

            if(unstableUnless(semverValidate(SEMVER_INCREMENT, readyForMerge()), "expected valid version increment but was $SEMVER_INCREMENT".toString(), problems)) {
              nextVersion = semverNextVersion(EXISTING_VERSION, SEMVER_INCREMENT) as String
              echo "planned version update: $EXISTING_VERSION -> $nextVersion".toString()
            }
          }

          sh 'printenv | sort'
        }
      }

      stage('Merge') {
        when { expression { nextVersion } }
        steps {
          mavenSetVersion nextVersion
          gitCommit "CI version $nextVersion (build $BUILD_ID)\n\n$GITHUB_PR_URL $GITHUB_PR_TITLE\n\n$GITHUB_PR_BODY"
        }
      }

      stage('Build') {
        steps {
          nginConfigureArtifactory(artifactory, rtMaven, buildInfo)
          // install is needed rather than package: https://www.jfrog.com/jira/projects/HAP/issues/HAP-957
          artifactoryMavenBuild pom: 'pom.xml', goals: 'clean install', mavenBuild: rtMaven, buildInfo: buildInfo
        }
      }

      stage('Report') {
        steps {
          script {
            String message = currentBuild.resultIsBetterOrEqualTo('SUCCESS') ? 'Build succeeded.' : "There were problems with the build: $problems"
            setGitHubPullRequestStatus message: message, context: JOB_NAME, state: currentBuild.currentResult
          }
        }
      }

      stage('Publish') {
        when { 
          allOf {
            expression { readyForMerge() }
            expression { currentBuild.resultIsBetterOrEqualTo('SUCCESS') }
          }
        }

        parallel {
          stage('Close pull request') {
            steps {
              gitPush()
            }
          }

          stage('Upload to Artifactory') {
            steps {
              deployArtifacts buildInfo: buildInfo, deployer: rtMaven.deployer
              publishBuildInfo buildInfo: buildInfo, server: artifactory
            }
          }
        }
      }
    }

    post {
      always {
        junit 'target/surefire-reports/**.xml'
        echo "problems: $problems"

        script {
          String message = currentBuild.resultIsBetterOrEqualTo('SUCCESS') ? 'Build succeeded.' : "There were problems with the build: $problems"
          setGitHubPullRequestStatus message: message, context: JOB_NAME, state: currentBuild.currentResult
        }
      }

      success {
        echo "buildResult: $currentBuild.result"
      }
    }
  }
}
