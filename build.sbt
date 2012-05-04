name := "play-sass"

version := "0.1.0"

sbtPlugin := true

organization := "net.litola"

description := "SBT plugin for handling Sass assets in Play 2.0"

/// Dependencies

libraryDependencies ++= Seq(
  "play" %% "play" % "2.0.1",
  "play" % "sbt-plugin" % "2.0.1" from "http://repo.typesafe.com/typesafe/releases/play/sbt-plugin/scala_2.9.1/sbt_0.11.2/2.0.1/jars/sbt-plugin.jar",
  "org.scalatest" %% "scalatest" % "1.7.1" % "test"
)
