def call(String message = "CI build ${BUILD_ID}", String userName = "${GIT_AUTHOR_NAME}", String userEmail = "${GIT_AUTHOR_EMAIL}") {
  sh "git -c user.name='${userName}' -c user.email='${userEmail}' commit -a -m '${message}'"
}
