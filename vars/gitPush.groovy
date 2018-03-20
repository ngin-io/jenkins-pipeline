def call(String sshKey='jenkins_github_ssh_key', String remote = UPSTREAM_REMOTE, String remoteBranch = UPSTREAM_BRANCH) {
  sshagent([sshKey]) {
    sh "git push ${remote} HEAD:${remoteBranch}"
  }
}
