package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class InputTypeConstraints(
  maxNumFields: Option[Int],
  minNumFields: Option[Int],
  fields: Option[String],
)
object InputTypeConstraints:
  given JsonValueCodec[InputTypeConstraints] = JsonCodecMaker.make
end InputTypeConstraints

case class InputTypeMeta(
  source: Option[String],
)
object InputTypeMeta:
  given JsonValueCodec[InputTypeMeta] = JsonCodecMaker.make
end InputTypeMeta

case class InputType(
  name: String,
  constraints: InputTypeConstraints,
  meta: Option[InputTypeMeta],
  fields: Seq[SchemaArg],
  fieldMap: Option[Map[String, SchemaArg]]
)
object InputType:
  given JsonValueCodec[InputType] = JsonCodecMaker.make
end InputType

