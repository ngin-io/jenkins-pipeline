import io.ngin.pipeline.semver.SemVer

def call(String body = GITHUB_PR_BODY, String tagKey = 'version') {
  SemVer.findTag(body, tagKey)
}
