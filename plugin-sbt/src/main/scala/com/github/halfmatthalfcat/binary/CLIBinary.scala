package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration
import com.github.halfmatthalfcat.util.Profile
import sys.process._
import os.Path
import sbt.Logger
import sttp.client3.{asFile, basicRequest}
import sttp.model.Uri

import java.io.{File, FileInputStream}
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

case object CLIBinary extends Binary {
  private[this] def getCliUrl(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.cliUrl}/prisma-cli-${config.cliVersion}-$platform-x64${getBinaryUrlExt(platform)}"

  private[binary] def getBinPath(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.resourcesDir}/prisma/${config.cliVersion}/prisma-cli-$platform"

  def binPath(
    implicit
    config: PrismaConfiguration,
  ): String = getBinPath(getPlatform)

  def ensure(
    implicit
    config: PrismaConfiguration,
    logger: Logger,
  ): Option[File] = {
    val platform = getPlatform
    val uncompressedDestPath = getBinPath(platform)
    val compressedDestPath = s"$uncompressedDestPath${getBinaryUrlExt(platform)}"

    val exists = os.exists(Path(uncompressedDestPath))

    if (exists) {
      logger.debug(s"[prisma] CLI binary exists, continuing.")
      Some(new File(uncompressedDestPath))
    } else {
      val compressedFile = new File(compressedDestPath)
      val cliUrl = Uri.unsafeParse(getCliUrl(platform))

      logger.debug(s"[prisma] Downloading CLI binary (${config.cliVersion}) as ${getCliUrl(platform)}")

      val (downloadNs, response) = Profile.fn(http.send(basicRequest.get(cliUrl).response(asFile(compressedFile))))

      response.body match {
        case Right(file) =>
          logger.debug(s"[prisma] Successfully downloaded CLI binary (${config.cliVersion}) from ${config.cliUrl} (${TimeUnit.NANOSECONDS.toMillis(downloadNs)}ms)")
          val (uncompressNs, _) = Profile.fn(os.write(
            Path(uncompressedDestPath),
            new GZIPInputStream(new FileInputStream(file))
          ))
          os.perms.set(Path(uncompressedDestPath), "r-xr-xr-x")
          os.remove(Path(compressedDestPath))
          logger.debug(s"[prisma] Successfully uncompressed CLI binary (${TimeUnit.NANOSECONDS.toMillis(uncompressNs)}ms)")
          Some(new File(uncompressedDestPath))
        case Left(_) =>
          logger.error(s"[prisma] Failed to download CLI binary (${config.cliVersion}) from ${config.cliUrl} with status ${response.code.code}")
          Option.empty
      }
    }
  }

  def generate(schemaPath: String)(
    implicit
    config: PrismaConfiguration,
    logger: Logger,
    query: QueryEngineBinary.type,
    migration: MigrationEngineBinary.type,
    introspection: IntrospectionEngineBinary.type,
    format: FormatBinary.type,
  ): Unit = {
    val envs = Seq(
      "PRISMA_CLI_QUERY_ENGINE_TYPE" -> "binary",
      "PRISMA_CLIENT_ENGINE_TYPE" -> "binary",
      "PRISMA_QUERY_ENGINE_BINARY" -> query.binPath,
      "PRISMA_MIGRATION_ENGINE_BINARY" -> migration.binPath,
      "PRISMA_INTROSPECTION_ENGINE_BINARY" -> introspection.binPath,
      "PRISMA_FMT_BINARY" -> format.binPath
    )
    Process(
      s"$binPath generate --schema=${schemaPath}",
      None,
      envs: _*,
    ).!(logger)
  }
}
