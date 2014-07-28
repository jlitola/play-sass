package net.litola

import play.PlayAssetsCompiler
import sbt.Keys._
import sbt._

object SassPlugin extends AutoPlugin with PlayAssetsCompiler {
  override def requires = sbt.plugins.JvmPlugin

  override def trigger = allRequirements

  object autoImport {
    val sassEntryPoints = settingKey[PathFinder]("Paths to Sass files to be compiled")
    val sassOptions = settingKey[Seq[String]]("Command line options for the sass command")

    val sassWatcher = AssetsCompiler("sass",
    { file => (file ** "*.sass") +++ (file ** "*.scss") },
    sassEntryPoints,
    { (name, min) =>
      name
        .replace(".sass", if (min) ".min.css" else ".css")
        .replace(".scss", if (min) ".min.css" else ".css")
    },
    { (file, options) => SassCompiler.compile(file, options) },
    sassOptions
    )

    lazy val baseSassSettings: Seq[Def.Setting[_]] = Seq(
      sassEntryPoints <<= (sourceDirectory in Compile)(base => ((base / "assets" ** "*.sass") +++ (base / "assets" ** "*.scss") --- base / "assets" ** "_*")),
      sassOptions := Seq.empty[String],
      resourceGenerators in Compile <+= sassWatcher
    )
  }

  import autoImport._

  override val projectSettings = baseSassSettings
}
