@NonCPS
def call(problems, currentBuild = currentBuild) {
  final boolean success = currentBuild.resultIsBetterOrEqualTo('SUCCESS')
  final String message = success ? 'Build succeeded.' : "There were problems with the build: $problems"
  setGitHubPullRequestStatus message: message, context: JOB_NAME, state: (success ? 'SUCCESS' : 'FAILURE') // GitHub can't understand "unstable"
}
