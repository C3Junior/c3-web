import _root_.scala.tools.nsc.MainGenericRunner
import org.aphreet.c3.Boot

object LiftConsole {
  def main(args: Array[String]) {
    // Instantiate your project's Boot file
    val b = new Boot()
    // Boot your project
    b.boot
    // Now run the MainGenericRunner to get your repl
    MainGenericRunner.main(args)
    // After the repl exits, then exit the scala script
    sys.exit(0)
  }
}
