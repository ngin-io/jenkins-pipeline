def call(String version, String maven = 'mvn') {
  sh "$maven versions:set -DnewVersion='${version}' -DgenerateBackupPoms=false"
}
