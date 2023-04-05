package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Field(
  name: String,
  kind: FieldKind.FieldKind,
  isList: Boolean,
  isRequired: Boolean,
  isUnique: Boolean,
  isId: Boolean,
  isReadOnly: Boolean,
  isGeneratedAt: Option[Boolean],
  isUpdatedAt: Option[Boolean],
  hasDefaultValue: Boolean,
  dbNames: Option[Seq[String]],
  `type`: TypeKind,
  default: Option[FieldDefaultValue],
  relationFromFields: Option[Seq[String]],
  relationToFields: Option[Seq[String]],
  relationOnDelete: Option[String],
  relationName: Option[String],
  documentation: Option[String],
)

object Field {
  implicit val codec: JsonValueCodec[Field] = JsonCodecMaker.make
}
