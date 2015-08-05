package net.litola

import java.io._

import sbt.Keys._
import sbt._

object SassPlugin extends AutoPlugin {
  override def requires = sbt.plugins.JvmPlugin

  override def trigger = allRequirements

  val sassEntryPoints = settingKey[PathFinder]("Paths to Sass files to be compiled")
  val sassOptions = settingKey[Seq[String]]("Command line options for the sass command")

  object autoImport {
    val naming: (String, Boolean) => String = { (name, min) =>
      name
        .replace(".sass", if (min) ".min.css" else ".css")
        .replace(".scss", if (min) ".min.css" else ".css")
    }

    val watchAndCompile = (state, sourceDirectory in Compile, resourceManaged in Compile, cacheDirectory, sassEntryPoints, sassOptions).map { (state, src, resource, cache, files, options) =>

      val cacheFile = cache / "sass"

      val sassFiles: Seq[File] = { file: File => (file ** "*.sass") +++ (file ** "*.scss") }.apply(src).get

      val currentInfos: Map[File, ModifiedFileInfo] = sassFiles.map(f => f -> FileInfo.lastModified(f)).toMap

      val (previousRelation, previousInfo) = Sync.readInfo(cacheFile)(FileInfo.lastModified.format)

      if (previousInfo != currentInfos) {
        //a changed file can be either a new file, a deleted file or a modified one
        lazy val changedFiles: Seq[File] = currentInfos.filter(e => !previousInfo.get(e._1).isDefined || previousInfo(e._1).lastModified < e._2.lastModified).map(_._1).toSeq ++ previousInfo.filter(e => !currentInfos.get(e._1).isDefined).map(_._1).toSeq

        //erease dependencies that belong to changed files
        val dependencies = previousRelation.filter((original, compiled) => changedFiles.contains(original))._2s
        dependencies.foreach(IO.delete)

        /**
         * If the given file was changed or
         * if the given file was a dependency,
         * otherwise calculate dependencies based on previous relation graph
         */
        val generated: Seq[(File, java.io.File)] = (files x relativeTo(Seq(src / "assets"))).flatMap {
          case (sourceFile, fileName) =>
            if (changedFiles.contains(sourceFile) || dependencies.contains(new File(resource, "public/" + naming(fileName, false)))) {

              val (debug, min, dependencies) = SassCompiler.compile(sourceFile, options)

              val out = new File(resource, "public/" + naming(fileName, false))
              IO.write(out, debug)
              (dependencies ++ Seq(sourceFile)).toSet[File].map(_ -> out) ++ min.map { minified =>
                val outMin = new File(resource, "public/" + naming(fileName, true))
                IO.write(outMin, minified)
                (dependencies ++ Seq(sourceFile)).map(_ -> outMin)
              }.getOrElse(Nil)
            } else {
              previousRelation.filter((original, compiled) => original == sourceFile)._2s.map(sourceFile -> _)
            }
        }

        //write object graph to cache file
        Sync.writeInfo(cacheFile,
          Relation.empty[File, File] ++ generated,
          currentInfos)(FileInfo.lastModified.format)

        // Return new files
        generated.map(_._2).distinct.toList

      } else {
        previousRelation._2s.toSeq
      }

    }

    lazy val baseSassSettings: Seq[Def.Setting[_]] = Seq(
      sassEntryPoints <<= (sourceDirectory in Compile)(base => ((base / "assets" ** "*.sass") +++ (base / "assets" ** "*.scss") --- base / "assets" ** "_*")),
      sassOptions := Seq.empty[String],
      resourceGenerators in Compile <+= watchAndCompile
    )
  }

  import autoImport._

  override val projectSettings = baseSassSettings
}
