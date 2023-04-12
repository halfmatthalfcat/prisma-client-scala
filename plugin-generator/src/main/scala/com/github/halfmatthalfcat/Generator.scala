package com.github.halfmatthalfcat

import com.github.halfmatthalfcat.config.Config
import com.github.halfmatthalfcat.prisma.rpc._
import com.github.halfmatthalfcat.prisma.scala.ModelConversion
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString, writeToString}
import org.scalafmt.Scalafmt._
import os.Path
import scopt.OParser

import scala.util.{Failure, Success, Try}

/**
 * A Prisma Generator is essentially an RPC-esque interface that consumes calls via stdin and emits responses
 * via stderr from the Prisma generation runtime (aka the migration-engine). This is the entrypoint for that
 * process.
 */

object Generator {

  def main(args: Array[String]): Unit = OParser.parse(Config.argParser, args, Config()) match {
    case Some(config) => listen(config)
    case _ => sys.exit(1)
  }

  private def listen(config: Config): Unit = io
    .Source
    .stdin
    .getLines()
    .map(str => Try(readFromString[RPCRequest[_]](str)))
    .collect {
      case Success(req) => req
      case Failure(_) => sys.exit(1)
    }
    .foreach {
      case genReq: GeneratorConfigRequest => send(GeneratorConfigResponse(
        id = genReq.id,
        result = GeneratorManifest(
          prettyName = buildinfo.BuildInfo.name,
          version = buildinfo.BuildInfo.version,
          defaultOutput = Some(config.output)
        ).asResponse
      ))
      case manReq: ManifestRequest =>
        val models = manReq
          .params
          .dmmf
          .datamodel
          .models
          .map(ModelConversion.getModel)
          .map(_.syntax)
          .map(syntax => format(syntax).get)
          .mkString("\n")
        os.makeDir.all(Path(config.output))
        os.write.over(Path(s"${config.output}/Prisma.scala"), models)
        sys.exit()
    }

  private def send[T: JsonValueCodec](response: T): Unit = {
    System.err.println(writeToString(response))
  }
}
