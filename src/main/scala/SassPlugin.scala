package net.litola

import sbt._
import sbt.Keys._
import play.Project._

object SassPlugin extends Plugin {
    val sassEntryPoints = SettingKey[PathFinder]("play-sass-entry-points")
    val sassOptions = SettingKey[Seq[String]]("play-sass-options")
    val sassWatcher = AssetsCompiler("sass",
        { file => (file ** "*.sass") +++ (file ** "*.scss") },
        sassEntryPoints,
        { (name, min) => 
            val tmp = name.replace(".sass", if (min) ".min.css" else ".css") 
            tmp.replace(".scss", if (min) ".min.css" else ".css")
        },
        { SassCompiler.compile _ },
        sassOptions
    )

    override val settings = Seq(
        sassEntryPoints <<= (sourceDirectory in Compile)(base => ((base / "assets" ** "*.sass") +++ (base / "assets" ** "*.scss") --- base / "assets" ** "_*")), 
        sassOptions := Seq.empty[String],
        resourceGenerators in Compile <+= sassWatcher
    )
}


// vim: set ts=4 sw=4 et:
