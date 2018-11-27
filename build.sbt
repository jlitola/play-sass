name := "play-sass"

version := "0.5.0"

sbtPlugin := true

organization := "net.litola"

description := "SBT plugin for handling Sass assets in Play 2.4"

/// Dependencies

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.2.4" % "test"
)

addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.0.2")

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

useGpg:= true

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/jlitola/play-sass</url>
  <licenses>
    <license>
      <name>MIT-style</name>
      <url>https://github.com/jlitola/play-sass/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:jlitola/play-sass.git</url>
    <connection>scm:git:git@github.com:jlitola/play-sass.git</connection>
  </scm>
  <developers>
    <developer>
      <id>jlitola</id>
      <name>Juha Litola</name>
      <url>https://github.com/jlitola</url>
    </developer>
  </developers>
)
