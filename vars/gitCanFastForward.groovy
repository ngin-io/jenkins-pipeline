@NonCPS
boolean call(String remote = UPSTREAM_REMOTE, String branch = UPSTREAM_BRANCH) {
  def retVal = sh returnStatus: true, script: "git merge-base --is-ancestor HEAD $remote/$branch"
  return (retVal == 0)
}
