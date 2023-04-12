package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Deprecation(
  sinceVersion: String,
  reason: String,
  plannedRemovalVersion: Option[String]
)

object Deprecation {
  implicit val codec: JsonValueCodec[Deprecation] = JsonCodecMaker.make
}