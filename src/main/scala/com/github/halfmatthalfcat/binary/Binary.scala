package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration

import sys.process.*
import os.*
import com.github.halfmatthalfcat.util.Profile
import sbt.Logger
import sttp.client3.{HttpClientSyncBackend, asFile, basicRequest}
import sttp.model.Uri

import java.io.{File, FileInputStream}
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

case class Binary(name: String) {
  private[this] lazy val http = HttpClientSyncBackend()
  private[this] lazy val idRegex = """(?m)^ID="?([^"\n]*)"?""".r
  private[this] lazy val idLikeRegex = """(?m)^ID_LIKE="?([^"\n]*)"?""".r

  private[this] def findLinuxDistro(): String = {
    val distro = "cat /etc/os-release".!!
    (idRegex.findFirstIn(distro) match {
      case Some(found) => Some(found)
      case None => idLikeRegex.findFirstIn(distro)
    }).map {
      case alpine if alpine == "alpine" => "alpine"
      case rhel if (
        rhel.contains("centos") ||
        rhel.contains("fedora") ||
        rhel.contains("rhel") ||
        rhel == "fedora"
      ) => "rhel"
      case debian if (
        debian.contains("debian") ||
        debian.contains("ubuntu") ||
        debian == "debian"
      ) => "debian"
    }.getOrElse("debian")
  }
  private[this] def getPlatform: String = sys
    .props
    .get("os.name")
    .map(_.toLowerCase)
    .getOrElse(sys.error("Unknown OS")) match {
      case mac if mac.contains("mac") => "darwin"
      case win if win.contains("win") => "windows"
      case linux if linux.contains("linux") => findLinuxDistro()
    }

  private[this] def getBinaryUrlExt(platform: String): String =
    if (platform == "windows") { ".exe.gz" } else { ".gz" }

  private[this] def getBinaryUrl(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.engineUrl}/${config.engineVersion}/$platform/$name${getBinaryUrlExt(platform)}"

  private[this] def getDestPath(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.outDir}/${config.engineVersion}/prisma-$name-$platform"

  def ensure()(
    implicit
    config: PrismaConfiguration,
    logger: Logger
  ): Option[File] = {
    val platform = getPlatform
    val compressedDestPath = getDestPath(platform)
    val uncompressedDestPath = s"$compressedDestPath${getBinaryUrlExt(platform)}"

    val exists = os.exists(Path(uncompressedDestPath))

    if (exists) {
      logger.debug(s"Binary $name exists, continuing.")
      Some(new File(uncompressedDestPath))
    } else {
      val compressedFile = new File(compressedDestPath)
      val binaryUrl = Uri.unsafeParse(getBinaryUrl(platform))

      logger.debug(s"Downloading binary $name (${config.engineVersion}) as ${getBinaryUrl(platform)}")

      val (downloadNs, response) = Profile.fn(http.send(basicRequest.get(binaryUrl).response(asFile(compressedFile))))

      response.body match {
        case Right(file) =>
          logger.debug(s"Successfully downloaded binary $name (${config.engineVersion}) from ${config.engineUrl} (${TimeUnit.NANOSECONDS.toMillis(downloadNs)}ms)")
          val (uncompressNs, _) = Profile.fn(os.write(
            Path(uncompressedDestPath),
            new GZIPInputStream(new FileInputStream(file))
          ))
          os.remove(Path(compressedDestPath))
          logger.debug(s"Successfully uncompressed binary $name (${TimeUnit.NANOSECONDS.toMillis(uncompressNs)}ms)")
          Some(new File(uncompressedDestPath))
        case Left(_) =>
          logger.error(s"Failed to download binary $name (${config.engineVersion}) from ${config.engineUrl} with status ${response.code.code}")
          Option.empty
      }
    }
  }
}
