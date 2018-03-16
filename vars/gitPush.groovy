def call(String sshKey='git-ssh-key', String remote='origin', String remoteBranch='master') {
  sshagent([sshKey]) {
    sh "git push ${remote} HEAD:${remoteBranch}"
  }
}
