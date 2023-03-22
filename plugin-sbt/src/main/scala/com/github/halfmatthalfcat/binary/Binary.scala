package com.github.halfmatthalfcat.binary

import com.github.halfmatthalfcat.PrismaConfiguration
import sbt.Logger
import sttp.client3.{HttpClientSyncBackend, Identity, SttpBackend}

import java.io.File

trait Binary {
  protected lazy val http: SttpBackend[Identity, Any] = HttpClientSyncBackend()
  protected def getPlatform: String = sys
    .props
    .get("os.name")
    .map(_.toLowerCase)
    .getOrElse(sys.error("Unknown OS")) match {
    case mac if mac.contains("mac") => "darwin"
    case win if win.contains("win") => "windows"
    case linux if linux.contains("linux") => "linux"
  }

  protected def getBinaryUrlExt(platform: String): String =
    if (platform == "windows") {
      ".exe.gz"
    } else {
      ".gz"
    }

  def ensure()(
    implicit
    config: PrismaConfiguration,
    logger: Logger,
  ): Option[File]
}
