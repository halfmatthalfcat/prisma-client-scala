package com.github.halfmatthalfcat

import com.github.halfmatthalfcat.binary.Binaries
import com.github.halfmatthalfcat.util.Schema
import sbt.{AutoPlugin, Def, *}
import sbt.Keys.*

import java.nio.file.Paths

object PrismaPlugin
  extends AutoPlugin
  with PrismaKeys
  with Binaries {

  private[this] val pluginName: String = "prisma-client-scala"

  override def requires = plugins.JvmPlugin

  override def trigger = allRequirements

  private[this] def prismaGenerateTask = Def.taskDyn {
    implicit val logger: Logger = streams.value.log
    implicit val config: PrismaConfiguration = PrismaConfiguration(
      outDir = prismaOutDir.value.getAbsolutePath,
      schemaFile = prismaSchemaFile.value.getAbsolutePath,
      resourcesDir = (Compile / resourceManaged).value.getAbsolutePath,
      scalaVersion = prismaScalaVersion.value,
    )
    Def.task {
      ensureBinaries()
      Schema.ensureGenerator(config.resourcesDir)
      val schemaPath = Schema.loadSchema(
        config.schemaFile,
        config.resourcesDir,
        config.outDir,
        config.scalaVersion,
      )
      cli.generate(schemaPath)
    }
  }

  override lazy val projectSettings: Seq[Def.Setting[?]] = super.projectSettings ++ Seq(
    prismaOutDir := (Compile / sourceManaged).value / "prisma",
    prismaSchemaFile := baseDirectory.value / "schema.prisma",
    prismaScalaVersion := ScalaVersion.`2`,
    prisma := prismaGenerateTask.value,
  )
}
