def call(boolean condition, String errorMessage, List<String> errors) {
  if(!condition) {
    errors << errorMessage
    if(currentBuild.resultIsBetterOrEqualTo('UNSTABLE')) {
      currentBuild.result = 'UNSTABLE'
    }
  }

  return condition
}
