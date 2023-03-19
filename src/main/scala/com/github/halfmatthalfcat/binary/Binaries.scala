package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration
import sbt.Logger

trait Binaries {
  lazy val query = Binary("query-engine")
  lazy val migration = Binary("migration-engine")
  lazy val introspection = Binary("introspection-engine")
  lazy val fmt = Binary("prisma-fmt")

  private[this] val binaries = Seq(
    query,
    migration,
    introspection,
    fmt,
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
