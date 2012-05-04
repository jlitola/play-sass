import sbt._
import sbt.Keys._

object PluginBuild extends Build {

  lazy val playSass = Project(
    id = "play-sass", base = file(".")
  )

}
