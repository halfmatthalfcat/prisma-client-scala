package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Mappings(
  modelOperations: Seq[ModelMapping],
  otherOperations: OtherOperationMappings,
)
object Mappings {
  implicit val codec: JsonValueCodec[Mappings] = JsonCodecMaker.make
}

