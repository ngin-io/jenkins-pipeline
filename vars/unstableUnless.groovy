def call(boolean condition, String errorMessage, String errors) {
  if(!condition) {
    errors << errorMessage
    if(currentBuild.resultIsBetterOrEqualTo('UNSTABLE')) {
      currentBuild.currentResult = 'UNSTABLE'
    }
  }

  return condition
}
