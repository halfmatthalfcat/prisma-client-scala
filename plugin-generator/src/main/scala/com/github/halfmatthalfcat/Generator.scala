package com.github.halfmatthalfcat

import com.github.halfmatthalfcat.prisma.rpc._
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString, writeToString}

import scala.util.{Failure, Success, Try}

/**
 * A Prisma Generator is essentially an RPC-esque interface that consumes calls via stdin and emits responses
 * via stderr from the Prisma generation runtime (aka the migration-engine). This is the entrypoint for that
 * process.
 */

object Generator {

  def main(args: Array[String]): Unit = {
    io
      .Source
      .stdin
      .getLines()
      .map(str => Try(readFromString[RPCRequest[_]](str)))
      .collect {
        case Success(req) => req
        case Failure(exception) => println(exception)
      }
      .foreach {
        case genReq: GeneratorConfigRequest => send(GeneratorConfigResponse(
          id = genReq.id,
          result = GeneratorManifest(
            prettyName = buildinfo.BuildInfo.name,
            version = buildinfo.BuildInfo.version,
            defaultOutput = Some("/Users/matt/Code/prisma-client-scala/plugin-generator/target/scala-3.2.2/src_managed/main/prisma")
          ).asResponse
        ))
        case manReq: ManifestRequest =>
          sys.exit()
      }
  }

  private def send[T: JsonValueCodec](response: T): Unit = {
    System.err.println(writeToString(response))
  }
}
