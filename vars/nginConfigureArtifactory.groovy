@NonCPS
def call(artifactory, rtMaven, buildInfo,
    releaseRepo = 'libs-release-local', mavenTool = 'maven3') {
  script {
    rtMaven.tool = 'maven3'
    rtMaven.resolver server: artifactory, releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot'
    rtMaven.deployer server: artifactory, releaseRepo: 'libs-release-local'
    rtMaven.deployer.deployArtifacts = false

    [
      'GIT_COMMIT',
      'GITHUB_PR_URL',
      'GITHUB_PR_TITLE',
      'GITHUB_PR_BODY',
      'GITHUB_PR_TRIGGER_SENDER_AUTHOR',
    ].each { buildInfo.env.filter.addInclude it }
    buildInfo.env.capture = true
  }
}
