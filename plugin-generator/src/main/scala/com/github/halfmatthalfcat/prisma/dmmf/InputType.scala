package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class InputTypeConstraints(
  maxNumFields: Option[Int],
  minNumFields: Option[Int],
  fields: Option[String],
)
object InputTypeConstraints {
  implicit val codec: JsonValueCodec[InputTypeConstraints] = JsonCodecMaker.make
}

case class InputTypeMeta(
  source: Option[String],
)
object InputTypeMeta {
  implicit val codec: JsonValueCodec[InputTypeMeta] = JsonCodecMaker.make
}

case class InputType(
  name: String,
  constraints: InputTypeConstraints,
  meta: Option[InputTypeMeta],
  fields: Seq[SchemaArg],
  fieldMap: Option[Map[String, SchemaArg]]
)
object InputType {
  implicit val codec: JsonValueCodec[InputType] = JsonCodecMaker.make
}

