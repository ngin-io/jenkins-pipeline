@NonCPS
def call(nextVersion = nextVersion, problems = problems, test = testResults, merged = merged, existingVersion = EXISTING_VERSION, jobName = JOB_NAME) {
  String pr = "<$GITHUB_PR_URL|#$GITHUB_PR_NUMBER $GITHUB_PR_TITLE>"

  String status = problems ? "Build problems in $pr: $problems (<${BUILD_URL}console|console output>)" :
      (merged ? "Merged $pr" : "Validation succeeded for $pr")

  String TEST_REPORT_URL = BUILD_URL + 'testReport/'
  String testResults = "<$TEST_REPORT_URL|Test results:> $test.passCount passed, $test.failCount failed, $test.skipCount skipped"

//  throw new RuntimeException("boo")

  String message = """*$JOB_NAME*: $existingVersion ⟶ $nextVersion
$status
$testResults"""

  String color = (currentBuild.resultIsBetterOrEqualTo('SUCCESS') && !problems) ? 'good' : 'danger'

  echo 'updating Slack'
  slackSend color: color, message: message
}
