def call(boolean condition, String errorMessage, List<String> errors) {
  if(!condition) {
    errors << errorMessage
    if(currentBuild.resultIsBetterOrEqualTo('UNSTABLE')) {
      currentBuild.currentResult = 'UNSTABLE'
    }
  }

  return condition
}
