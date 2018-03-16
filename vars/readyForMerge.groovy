def call(Collection<String> labels = GITHUB_PR_LABELS.split(',').toList()) {
  return labels.contains('ready for CI')
}
