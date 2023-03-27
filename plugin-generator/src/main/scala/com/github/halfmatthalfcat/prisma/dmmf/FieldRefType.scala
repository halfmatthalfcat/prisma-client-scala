package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.util.Try

type FieldRefAllowType = TypeRefScalar | TypeRefEnum
given JsonValueCodec[FieldRefAllowType] = new JsonValueCodec[FieldRefAllowType]:
  override def decodeValue(in: JsonReader, default: FieldRefAllowType): FieldRefAllowType = {
    if (in.isNextToken('{')) {
      val trs = Try(summon[JsonValueCodec[TypeRefScalar]].decodeValue(in, null))
      if (trs.isSuccess) return trs.get
      val tre = Try(summon[JsonValueCodec[TypeRefEnum]].decodeValue(in, null))
      if (tre.isSuccess) tre.get
      else in.decodeError("expected TypeRefScalar or TypeRefEnum")
    } else in.decodeError("expected TypeRefScalar or TypeRefEnum")
  }

  override def encodeValue(x: FieldRefAllowType, out: JsonWriter): Unit = x match {
    case trs: TypeRefScalar => summon[JsonValueCodec[TypeRefScalar]].encodeValue(trs, out)
    case tre: TypeRefEnum => summon[JsonValueCodec[TypeRefEnum]].encodeValue(tre, out)
  }

  override def nullValue: FieldRefAllowType = null
end given

case class FieldRefType(
  name: String,
  allowTypes: Seq[FieldRefAllowType],
  fields: Seq[SchemaArg],
)
object FieldRefType:
  given JsonValueCodec[FieldRefType] = JsonCodecMaker.make
end FieldRefType
