package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class DatamodelEnum(
  name: String,
  values: Seq[EnumValue],
  dbName: Option[String],
  documentation: String,
)
object DatamodelEnum:
  given JsonValueCodec[DatamodelEnum] = JsonCodecMaker.make
end DatamodelEnum
