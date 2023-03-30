package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class OutputType(
  name: String,
  fields: Seq[SchemaField],
  fieldMap: Option[Map[String, SchemaField]]
)
object OutputType {
  implicit val codec: JsonValueCodec[OutputType] = JsonCodecMaker.make
}
