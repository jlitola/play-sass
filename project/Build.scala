import sbt._
import sbt.Keys._

object PluginBuild extends Build {

  lazy val playSass = Project(
    id = "play-sass", base = file(".")
  ).settings(scalaVersion := "2.10.4")

}
