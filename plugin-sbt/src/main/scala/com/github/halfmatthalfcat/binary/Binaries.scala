package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration
import sbt.Logger

trait Binaries {
  implicit val query: QueryEngineBinary.type = QueryEngineBinary
  implicit val migration: MigrationEngineBinary.type = MigrationEngineBinary
  implicit val introspection: IntrospectionEngineBinary.type = IntrospectionEngineBinary
  implicit val format: FormatBinary.type = FormatBinary
  implicit val cli: CLIBinary.type = CLIBinary

  private[this] val binaries = Seq(
    query,
    migration,
    introspection,
    format,
    cli,
  )

  def ensureBinaries()(
    implicit
    config: PrismaConfiguration,
    logger: Logger
  ): Boolean = {
    val binaryCount = binaries
      .toIterator
      .map(_.ensure)
      .takeWhile(_.nonEmpty)
      .toList
      .size

    binaryCount == binaries.size
  }
}
