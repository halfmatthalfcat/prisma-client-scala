package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class EnumValue(
  name: String,
  dbName: Option[String],
)

object EnumValue {
  implicit val codec: JsonValueCodec[EnumValue] = JsonCodecMaker.make
}
