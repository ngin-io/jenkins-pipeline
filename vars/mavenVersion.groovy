/*
 * Note that this step requires Maven to be available on the default path.
 * The tools section of a pipeline isn't run until after the environment
 * is evaluated, and using withMaven results in unhelpful extra stdout.
 */
def call() {
  def version = sh(
    returnStdout: true,
    script: "mvn -q -Dexec.executable='echo' -Dexec.args='\${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.6.0:exec"
  )
  return version
}
