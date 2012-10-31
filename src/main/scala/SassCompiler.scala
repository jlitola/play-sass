package net.litola

import sbt.PlayExceptions.AssetCompilationException
import java.io.File
import scala.sys.process._
import sbt.IO
import io.Source._

object SassCompiler {
  def compile(sassFile: File, options: Seq[String]): (String, Option[String], Seq[File]) = {
    try {
      val parentPath = sassFile.getParentFile.getAbsolutePath
      val (cssOutput, dependencies) = runCompiler(
        Seq(sassCommand, "-l", "-I", parentPath) ++ options ++ Seq(sassFile.getAbsolutePath)
        )
      val (compressedCssOutput, ignored) = runCompiler(
        Seq(sassCommand, "-t", "compressed", "-I", parentPath) ++ options ++ Seq(sassFile.getAbsolutePath)
        )

      (cssOutput, Some(compressedCssOutput), dependencies.map { new File(_) } )
    } catch {
      case e: SassCompilationException => {
        throw AssetCompilationException(e.file.orElse(Some(sassFile)), "Sass compiler: " + e.message, e.line, e.column)
      }
    }
  }

  private def sassCommand = if (isWindows) "sass.bat" else "sass"
  
  private val isWindows = System.getProperty("os.name").toLowerCase().indexOf("win") >= 0
 
  private val DependencyLine = """^/\* line \d+, (.*) \*/$""".r

  private def runCompiler(command: ProcessBuilder): (String, Seq[String]) = {
    val err = new StringBuilder
    val out = new StringBuilder

    val capturer = ProcessLogger(
      (output: String) => out.append(output + "\n"),
      (error: String) => err.append(error + "\n"))

    val process = command.run(capturer)
    if (process.exitValue == 0) {
      val dependencies = out.lines.collect {
          case DependencyLine(f) => f
        }

      (out.mkString, dependencies.toList.distinct)
    } else
      throw new SassCompilationException(err.toString)
  }

  private val LocationLine = """\s*on line (\d+) of (.*)""".r

  private class SassCompilationException(stderr: String) extends RuntimeException {

    val (file: Option[File], line: Int, column: Int, message: String) = parseError(stderr)

    private def parseError(error: String): (Option[File], Int, Int, String) = {
      var line = 0
      var seen = 0
      var column = 0
      var file : Option[File] = None
      var message = "Unknown error, try running sass directly"
      for (errline: String <- augmentString(error).lines) {
        errline match {
          case LocationLine(l, f) => { line = l.toInt; file = Some(new File(f)); }
          case other if (seen == 0) => { message = other; seen += 1 }
          case other =>
        }
      }
      (file, line, column, message)
    }
  }
}
