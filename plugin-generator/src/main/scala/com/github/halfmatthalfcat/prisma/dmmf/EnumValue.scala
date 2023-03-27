package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class EnumValue(
  name: String,
  dbName: Option[String],
)
object EnumValue:
  given JsonValueCodec[EnumValue] = JsonCodecMaker.make
end EnumValue
