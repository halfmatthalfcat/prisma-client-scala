package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

import scala.util.Try

type ArgType = String | InputType | SchemaEnum
given JsonValueCodec[ArgType] = new JsonValueCodec[ArgType]:
  override def decodeValue(in: JsonReader, default: ArgType): ArgType = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      in.readString(null)
    } else if (b == '{') {
      in.rollbackToken()
      val inputType = Try(summon[JsonValueCodec[InputType]].decodeValue(in, null))
      if (inputType.isSuccess) return inputType.get
      val schemaEnumType = Try(summon[JsonValueCodec[SchemaEnum]].decodeValue(in, null))
      if (schemaEnumType.isSuccess) return schemaEnumType.get
      in.decodeError("expected String, InputType or SchemaEnum")
    } else in.decodeError("expected String, InputType or SchemaEnum")
  }

  override def encodeValue(x: ArgType, out: JsonWriter): Unit = x match {
    case s: String => out.writeVal(s)
    case inputType: InputType => summon[JsonValueCodec[InputType]].encodeValue(inputType, out)
    case schemaEnum: SchemaEnum => summon[JsonValueCodec[SchemaEnum]].encodeValue(schemaEnum, out)
  }

  override def nullValue: ArgType = null
end given

case class SchemaArgInputType(
  `type`: ArgType,
  namespace: Option[String],
  location: FieldLocation,
  isList: Boolean,
)
object SchemaArgInputType:
  given JsonValueCodec[SchemaArgInputType] = JsonCodecMaker.make(
    CodecMakerConfig.withDiscriminatorFieldName(None)
  )
end SchemaArgInputType
