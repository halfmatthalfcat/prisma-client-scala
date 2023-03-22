package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration

import sys.process.*
import os.*
import com.github.halfmatthalfcat.util.Profile
import sbt.Logger
import sttp.client3.{asFile, basicRequest}
import sttp.model.Uri

import java.io.{File, FileInputStream}
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

case class EngineBinary(name: String) extends Binary {
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

  private[this] def getBinaryUrl(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.engineUrl}/${config.engineVersion}/$platform/$name${getBinaryUrlExt(platform)}"

  private[this] def getDestPath(platform: String)(
    implicit config: PrismaConfiguration
  ): String = s"${config.outDir}/${config.engineVersion}/prisma-$name-$platform"


  def ensure()(
    implicit
    config: PrismaConfiguration,
    logger: Logger,
  ): Option[File] = {
    val platform = getPlatform match {
      case "linux" => findLinuxDistro()
      case other => other
    }
    val uncompressedDestPath = getDestPath(platform)
    val compressedDestPath = s"$uncompressedDestPath${getBinaryUrlExt(platform)}"

    val exists = os.exists(Path(uncompressedDestPath))

    if (exists) {
      logger.debug(s"[prisma] Binary $name exists, continuing.")
      Some(new File(uncompressedDestPath))
    } else {
      val compressedFile = new File(compressedDestPath)
      val binaryUrl = Uri.unsafeParse(getBinaryUrl(platform))

      logger.debug(s"[prisma] Downloading binary $name (${config.engineVersion}) as ${getBinaryUrl(platform)}")

      val (downloadNs, response) = Profile.fn(http.send(basicRequest.get(binaryUrl).response(asFile(compressedFile))))

      response.body match {
        case Right(file) =>
          logger.debug(s"[prisma] Successfully downloaded binary $name (${config.engineVersion}) from ${config.engineUrl} (${TimeUnit.NANOSECONDS.toMillis(downloadNs)}ms)")
          val (uncompressNs, _) = Profile.fn(os.write(
            Path(uncompressedDestPath),
            new GZIPInputStream(new FileInputStream(file))
          ))
          os.remove(Path(compressedDestPath))
          logger.debug(s"[prisma] Successfully uncompressed binary $name (${TimeUnit.NANOSECONDS.toMillis(uncompressNs)}ms)")
          Some(new File(uncompressedDestPath))
        case Left(_) =>
          logger.error(s"[prisma] Failed to download binary $name (${config.engineVersion}) from ${config.engineUrl} with status ${response.code.code}")
          Option.empty
      }
    }
  }
}
