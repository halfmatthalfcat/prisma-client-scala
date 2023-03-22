package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration
import com.github.halfmatthalfcat.util.Profile
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

  private[this] def getDestPath(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.outDir}/${config.cliVersion}/prisma-cli-$platform"

  def ensure()(
    implicit
    config: PrismaConfiguration,
    logger: Logger,
  ): Option[File] = {
    val platform = getPlatform
    val uncompressedDestPath = getDestPath(platform)
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
          os.remove(Path(compressedDestPath))
          logger.debug(s"[prisma] Successfully uncompressed CLI binary (${TimeUnit.NANOSECONDS.toMillis(uncompressNs)}ms)")
          Some(new File(uncompressedDestPath))
        case Left(_) =>
          logger.error(s"[prisma] Failed to download CLI binary (${config.cliVersion}) from ${config.cliUrl} with status ${response.code.code}")
          Option.empty
      }
    }
  }
}
