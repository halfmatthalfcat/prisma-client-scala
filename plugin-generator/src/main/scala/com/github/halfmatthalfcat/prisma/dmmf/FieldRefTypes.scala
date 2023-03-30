package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class FieldRefTypes(
  model: Option[Seq[FieldRefType]],
  prisma: Option[Seq[FieldRefType]],
)
object FieldRefTypes {
  implicit val codec: JsonValueCodec[FieldRefTypes] = JsonCodecMaker.make
}
