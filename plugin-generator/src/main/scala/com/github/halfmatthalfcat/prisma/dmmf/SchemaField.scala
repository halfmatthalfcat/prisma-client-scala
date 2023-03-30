package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class SchemaField(
  name: String,
  isNullable: Option[Boolean],
  outputType: TypeRef,
  args: Seq[SchemaArg],
  deprecation: Option[Deprecation],
  documentation: Option[String],
)
object SchemaField {
  implicit val codec: JsonValueCodec[SchemaField] = JsonCodecMaker.make
}
