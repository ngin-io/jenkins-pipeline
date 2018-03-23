def call(String sshKey='git_ssh_key', String remote = UPSTREAM_REMOTE, String remoteBranch = UPSTREAM_BRANCH) {
  sshagent([sshKey]) {
    sh "git push ${remote} HEAD:${remoteBranch}"
  }
}
