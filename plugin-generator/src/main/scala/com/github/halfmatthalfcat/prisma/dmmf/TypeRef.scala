package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.*

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
object TypeRefScalar:
  given JsonValueCodec[TypeRefScalar] = JsonCodecMaker.make
end TypeRefScalar

type OutputObjectType = OutputType | String
given JsonValueCodec[OutputObjectType] = new JsonValueCodec[OutputObjectType]:
  override def decodeValue(in: JsonReader, default: OutputObjectType): OutputObjectType = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      in.readString(null)
    } else if (b == '{') {
      in.rollbackToken()
      summon[JsonValueCodec[OutputType]].decodeValue(in, null)
    } else in.decodeError("expected String or OutputType")
  }

  override def encodeValue(x: OutputObjectType, out: JsonWriter): Unit = x match {
    case s: String => out.writeVal(s)
    case o: OutputType => summon[JsonValueCodec[OutputType]].encodeValue(o, null)
  }

  override def nullValue: OutputObjectType = null
end given

@named("outputObjectTypes") case class TypeRefOutputObject(
  isList: Boolean,
  namespace: Option[FieldNamespace],
  `type`: OutputObjectType
) extends TypeRef

type EnumType = SchemaEnum | String
given JsonValueCodec[EnumType] = new JsonValueCodec[EnumType]:
  override def decodeValue(in: JsonReader, default: EnumType): EnumType = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      in.readString(null)
    } else if (b == '{') {
      in.rollbackToken()
      summon[JsonValueCodec[SchemaEnum]].decodeValue(in, null)
    } else in.decodeError("expected String or SchemaEnum")
  }

  override def encodeValue(x: EnumType, out: JsonWriter): Unit = x match {
    case s: String => out.writeVal(s)
    case s: SchemaEnum => summon[JsonValueCodec[SchemaEnum]].encodeValue(s, null)
  }

  override def nullValue: EnumType = null

@named("enumTypes") case class TypeRefEnum(
  isList: Boolean,
  namespace: Option[FieldNamespace],
  `type`: EnumType
) extends TypeRef
object TypeRefEnum:
  given JsonValueCodec[TypeRefEnum] = JsonCodecMaker.make
end TypeRefEnum
