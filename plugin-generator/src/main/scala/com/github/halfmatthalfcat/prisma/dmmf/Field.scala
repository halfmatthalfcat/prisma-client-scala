package com.github.halfmatthalfcat.prisma.dmmf

import scala.collection.mutable
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

case class Field(
  name: String,
  kind: FieldKind,
  isList: Boolean,
  isRequired: Boolean,
  isUnique: Boolean,
  isId: Boolean,
  isReadOnly: Boolean,
  isGeneratedAt: Option[Boolean],
  isUpdatedAt: Option[Boolean],
  hasDefaultValue: Boolean,
  dbNames: Option[Seq[String]],
  `type`: String,
  default: Option[FieldDefaultValue],
  relationFromFields: Option[Seq[String]],
  relationToFields: Option[Seq[String]],
  relationOnDelete: Option[String],
  relationName: Option[String],
  documentation: Option[String],
)
object Field:
  given JsonValueCodec[Field] = JsonCodecMaker.make
end Field

