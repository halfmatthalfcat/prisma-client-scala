package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class UniqueIndex(
  name: String,
  fields: Seq[String],
)
object UniqueIndex {
  implicit val codec: JsonValueCodec[UniqueIndex] = JsonCodecMaker.make
}
