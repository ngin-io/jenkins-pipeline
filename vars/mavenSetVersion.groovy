def call(String version, String maven = 'mvn') {
  withMaven {
    sh "$maven versions:set -DnewVersion='${version}' -DgenerateBackupPoms=false"
  }
}
