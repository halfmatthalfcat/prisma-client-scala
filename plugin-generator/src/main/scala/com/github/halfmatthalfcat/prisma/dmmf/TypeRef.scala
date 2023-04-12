package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros._

sealed trait TypeRef {
  val isList: Boolean
  val namespace: Option[FieldNamespace]
}
object TypeRef {
  implicit val codec: JsonValueCodec[TypeRef] = JsonCodecMaker.make(CodecMakerConfig
    .withDiscriminatorFieldName(Some("location"))
    .withRequireDiscriminatorFirst(false))
}

@named("scalar") case class TypeRefScalar(
  isList: Boolean,
  namespace: Option[FieldNamespace],
  `type`: String,
) extends TypeRef
object TypeRefScalar {
  implicit val codec: JsonValueCodec[TypeRefScalar] = JsonCodecMaker.make
}

@named("outputObjectTypes") case class TypeRefOutputObject(
  isList: Boolean,
  namespace: Option[FieldNamespace],
  `type`: OutputObjectType
) extends TypeRef

@named("enumTypes") case class TypeRefEnum(
  isList: Boolean,
  namespace: Option[FieldNamespace],
  `type`: EnumType
) extends TypeRef
object TypeRefEnum {
  implicit val codec: JsonValueCodec[TypeRefEnum] = JsonCodecMaker.make
}