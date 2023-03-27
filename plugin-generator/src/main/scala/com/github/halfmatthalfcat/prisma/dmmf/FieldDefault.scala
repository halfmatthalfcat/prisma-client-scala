package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class FieldDefault(
  name: String,
  args: Seq[FieldDefaultScalar]
)
object FieldDefault:
  given JsonValueCodec[FieldDefault] = JsonCodecMaker.make
end FieldDefault
