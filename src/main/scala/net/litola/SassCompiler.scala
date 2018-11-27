package net.litola


import sbt._
import java.io.File
import xsbti.Severity

import scala.sys.process._
import com.typesafe.sbt.web._

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

      (cssOutput, Some(compressedCssOutput), dependencies.map {
        new File(_)
      })
    } catch {
      case e: CompileProblemsException => throw e
    }
  }

  private def sassCommand = if (isWindows) "sass.bat" else "sass"

  private val isWindows = System.getProperty("os.name").toLowerCase.indexOf("win") >= 0

  private val DependencyLine = """^/\* line \d+, (.*) \*/$""".r

  private def runCompiler(command: scala.sys.process.ProcessBuilder): (String, Seq[String]) = {
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
    } else {
      val (file, line, column, message) = parseError(err.toString())
      if(file == null){
        throw new RuntimeException(s"Sass compilation error [ $message ].")
      } else {
        val error = "Compilation error [ " + message + " ]"
        val problem = new LineBasedProblem(error, Severity.Error, line, column, message, file)
        throw new CompileProblemsException(Array(problem))
      }
    }

  }

  private val LocationLine = """\s*on line (\d+) of (.*)""".r

  private def parseError(error: String): (File, Int, Int, String) = {
    var line = 0
    var seen = 0
    val column = 0
    var file: File = null
    var message = "Unknown error, try running sass directly"

    for (errline: String <- augmentString(error).lines) {
      errline match {
        case LocationLine(l, f) =>
          line = l.toInt
          file = new File(f)
        case other if seen == 0 =>
          message = other
          seen += 1
        case _ => // do nothing
      }
    }

    (file, line, column, message)
  }

}
