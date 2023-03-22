package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration
import sbt.Logger

trait Binaries {
  lazy val query = EngineBinary("query-engine")
  lazy val migration = EngineBinary("migration-engine")
  lazy val introspection = EngineBinary("introspection-engine")
  lazy val fmt = EngineBinary("prisma-fmt")

  private[this] val binaries = Seq(
    query,
    migration,
    introspection,
    fmt,
    CLIBinary
  )

  def ensureBinaries()(
    implicit
    config: PrismaConfiguration,
    logger: Logger
  ): Boolean = {
    val binaryCount = binaries
      .toIterator
      .map(_.ensure())
      .takeWhile(_.nonEmpty)
      .toList
      .size

    binaryCount == binaries.size
  }
}
