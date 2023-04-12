package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros._

import scala.util.Try

sealed trait ArgType
object ArgType {
  implicit val codec: JsonValueCodec[ArgType] = new JsonValueCodec[ArgType] {
    override def decodeValue(in: JsonReader, default: ArgType): ArgType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        IsArgTypeString(in.readString(null))
      } else if (b == '{') {
        in.rollbackToken()
        val inputType = Try(implicitly[JsonValueCodec[InputType]].decodeValue(in, null))
        if (inputType.isSuccess) return IsArgTypeInputType(inputType.get)
        val schemaEnumType = Try(implicitly[JsonValueCodec[SchemaEnum]].decodeValue(in, null))
        if (schemaEnumType.isSuccess) return IsArgTypeSchemaEnum(schemaEnumType.get)
        in.decodeError("expected String, InputType or SchemaEnum")
      } else in.decodeError("expected String, InputType or SchemaEnum")
    }

    override def encodeValue(x: ArgType, out: JsonWriter): Unit = x match {
      case IsArgTypeString(str) => out.writeVal(str)
      case IsArgTypeInputType(inputType) => implicitly[JsonValueCodec[InputType]].encodeValue(inputType, out)
      case IsArgTypeSchemaEnum(schemaEnum) => implicitly[JsonValueCodec[SchemaEnum]].encodeValue(schemaEnum, out)
    }

    override def nullValue: ArgType = null.asInstanceOf[ArgType]
  }
}

case class IsArgTypeString(str: String) extends ArgType
object IsArgTypeString {
  implicit def asStr(isString: IsArgTypeString): String = isString.str
  implicit def box(str: String): IsArgTypeString = IsArgTypeString(str)
}

case class IsArgTypeInputType(inputType: InputType) extends ArgType
object IsArgTypeInputType {
  implicit def asInputType(isInputType: IsArgTypeInputType): InputType = isInputType.inputType
  implicit def box(inputType: InputType): IsArgTypeInputType = IsArgTypeInputType(inputType)
}

case class IsArgTypeSchemaEnum(schemaEnum: SchemaEnum) extends ArgType
object IsArgTypeSchemaEnum {
  implicit def asSchemaEnum(isSchemaEnum: IsArgTypeSchemaEnum): SchemaEnum = isSchemaEnum.schemaEnum
  implicit def box(schemaEnum: SchemaEnum): IsArgTypeSchemaEnum = IsArgTypeSchemaEnum(schemaEnum)
}

case class SchemaArgInputType(
  `type`: ArgType,
  namespace: Option[String],
  location: FieldLocation,
  isList: Boolean,
)
object SchemaArgInputType {
  implicit val codec: JsonValueCodec[SchemaArgInputType] = JsonCodecMaker.make(
    CodecMakerConfig.withDiscriminatorFieldName(None)
  )
}
