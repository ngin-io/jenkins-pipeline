import io.ngin.pipeline.semver.SemVer

@NonCPS
String call(String body = GITHUB_PR_BODY, String tagKey = 'version') {
  SemVer.findTag(body, tagKey)
}
