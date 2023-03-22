package com.github.halfmatthalfcat

import com.github.halfmatthalfcat.binary.Binaries
import sbt.{AutoPlugin, Def, _}
import sbt.Keys._

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
      outDir = prismaOutDir.value
    )
    Def.task {
      ensureBinaries()
    }
  }

  override lazy val projectSettings: Seq[Def.Setting[?]] = super.projectSettings ++ Seq(
    prismaOutDir := s"${(Compile / target).value.getAbsolutePath}/prisma",
    prisma := prismaGenerateTask.value,
  )
}
