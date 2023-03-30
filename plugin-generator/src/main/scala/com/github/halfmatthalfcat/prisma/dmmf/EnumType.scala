package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed trait EnumType
object EnumType {
  implicit val codec: JsonValueCodec[EnumType] = new JsonValueCodec[EnumType] {
    override def decodeValue(in: JsonReader, default: EnumType): EnumType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        IsEnumTypeString(in.readString(null))
      } else if (b == '{') {
        in.rollbackToken()
        IsEnumTypeSchemaEnum(
          implicitly[JsonValueCodec[SchemaEnum]].decodeValue(in, null)
        )
      } else in.decodeError("expected String or SchemaEnum")
    }

    override def encodeValue(x: EnumType, out: JsonWriter): Unit = x match {
      case IsEnumTypeString(str) => out.writeVal(str)
      case IsEnumTypeSchemaEnum(schemaEnum) => 
        implicitly[JsonValueCodec[SchemaEnum]].encodeValue(schemaEnum, null)
    }

    override def nullValue: EnumType = null.asInstanceOf[EnumType]
  }
}

case class IsEnumTypeString(str: String) extends EnumType
object IsEnumTypeString {
  implicit def asString(isEnumTypeString: IsEnumTypeString): String =
    isEnumTypeString.str
  implicit def box(str: String): IsEnumTypeString = IsEnumTypeString(str)
}

case class IsEnumTypeSchemaEnum(schemaEnum: SchemaEnum) extends EnumType
object IsEnumTypeSchemaEnum {
  implicit def asSchemaEnum(isEnumTypeSchemaEnum: IsEnumTypeSchemaEnum): SchemaEnum =
    isEnumTypeSchemaEnum.schemaEnum
  implicit def box(schemaEnum: SchemaEnum): IsEnumTypeSchemaEnum = 
    IsEnumTypeSchemaEnum(schemaEnum)
}