@NonCPS
def call(String message = "CI build ${BUILD_ID}", String author = "${GIT_AUTHOR}") {
  final String HERE = 'JENKINS_HERE_DOCUMENT_DELIMITER--'

  String m = groovy.json.StringEscapeUtils.unescapeJava(message)

  sh """git commit --author='$author' -a -F - <<$HERE
$m
$HERE"""
}
