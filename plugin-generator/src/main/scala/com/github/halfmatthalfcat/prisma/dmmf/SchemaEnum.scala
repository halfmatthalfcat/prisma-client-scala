package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class SchemaEnum(
  name: String,
  values: Seq[String]
)
object SchemaEnum:
  given JsonValueCodec[SchemaEnum] = JsonCodecMaker.make
end SchemaEnum
