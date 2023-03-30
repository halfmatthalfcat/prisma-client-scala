package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.util.Try

sealed trait FieldRefAllowType
object FieldRefAllowType {
  implicit val codec: JsonValueCodec[FieldRefAllowType] = new JsonValueCodec[FieldRefAllowType] {
    override def decodeValue(in: JsonReader, default: FieldRefAllowType): FieldRefAllowType = {
      if (in.isNextToken('{')) {
        val trs = Try(implicitly[JsonValueCodec[TypeRefScalar]].decodeValue(in, null))
        if (trs.isSuccess) return IsTypeRefScalar(trs.get)
        val tre = Try(implicitly[JsonValueCodec[TypeRefEnum]].decodeValue(in, null))
        if (tre.isSuccess) IsTypeRefEnum(tre.get)
        else in.decodeError("expected TypeRefScalar or TypeRefEnum")
      } else in.decodeError("expected TypeRefScalar or TypeRefEnum")
    }

    override def encodeValue(x: FieldRefAllowType, out: JsonWriter): Unit = x match {
      case IsTypeRefScalar(trs) => implicitly[JsonValueCodec[TypeRefScalar]].encodeValue(trs, out)
      case IsTypeRefEnum(tre) => implicitly[JsonValueCodec[TypeRefEnum]].encodeValue(tre, out)
    }

    override def nullValue: FieldRefAllowType = null.asInstanceOf[FieldRefAllowType]
  }
}

case class IsTypeRefScalar(trs: TypeRefScalar) extends FieldRefAllowType
object IsTypeRefScalar {
  implicit def asTypeRefScalar(isTypeRefScalar: IsTypeRefScalar): TypeRefScalar =
    isTypeRefScalar.trs
  implicit def box(trs: TypeRefScalar): IsTypeRefScalar = IsTypeRefScalar(trs)
}

case class IsTypeRefEnum(tre: TypeRefEnum) extends FieldRefAllowType
object IsTypeRefEnum {
  implicit def asTypeRefEnum(isTypeRefEnum: IsTypeRefEnum): TypeRefEnum =
    isTypeRefEnum.tre
  implicit def box(tre: TypeRefEnum): IsTypeRefEnum = IsTypeRefEnum(tre)
}

case class FieldRefType(
  name: String,
  allowTypes: Seq[FieldRefAllowType],
  fields: Seq[SchemaArg],
)
object FieldRefType {
  implicit val codec: JsonValueCodec[FieldRefType] = JsonCodecMaker.make
}
