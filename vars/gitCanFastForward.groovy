//@NonCPS: retVal isn't properly handled in NonCPS, no idea why
boolean call(String remote = UPSTREAM_REMOTE, String branch = UPSTREAM_BRANCH) {
  def retVal = sh(returnStatus: true, script: "git merge-base --is-ancestor $remote/$branch HEAD")
  echo "merge-base retval: $retVal"
  return (retVal == 0)
}
