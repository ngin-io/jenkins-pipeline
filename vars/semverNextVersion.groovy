import io.ngin.pipeline.semver.SemVer

@NonCPS
SemVer call(String currentVersion, String increment) {
  SemVer.increment(currentVersion, increment)
}
