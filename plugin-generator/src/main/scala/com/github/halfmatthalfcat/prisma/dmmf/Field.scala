package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Field(
  name: String,
  kind: FieldKind,
  isList: Boolean,
  isRequired: Boolean,
  isUnique: Boolean,
  isId: Boolean,
  isReadOnly: Boolean,
  isGeneratedAt: Option[Boolean] = None,
  isUpdatedAt: Option[Boolean] = None,
  hasDefaultValue: Boolean,
  dbNames: Option[Seq[String]] = None,
  `type`: TypeKind,
  default: Option[FieldDefaultValue] = None,
  relationFromFields: Option[Seq[String]] = None,
  relationToFields: Option[Seq[String]] = None,
  relationOnDelete: Option[String] = None,
  relationName: Option[String] = None,
  documentation: Option[String] = None,
)

object Field {
  implicit val codec: JsonValueCodec[Field] = JsonCodecMaker.make
}
