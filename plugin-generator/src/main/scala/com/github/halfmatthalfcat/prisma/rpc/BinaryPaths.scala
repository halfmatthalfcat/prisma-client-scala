package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class BinaryPaths(
  migrationEngine: Option[Map[String, String]],
  queryEngine: Option[Map[String, String]],
  libqueryEngine: Option[Map[String, String]],
)
object BinaryPaths {
  implicit val codec: JsonValueCodec[BinaryPaths] = JsonCodecMaker.make
}
