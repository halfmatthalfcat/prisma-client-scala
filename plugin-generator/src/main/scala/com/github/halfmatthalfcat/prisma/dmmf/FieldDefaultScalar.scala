package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

type FieldDefaultScalar = String | Int | Boolean
given JsonValueCodec[FieldDefaultScalar] = new JsonValueCodec[FieldDefaultScalar]:
  override def decodeValue(in: JsonReader, default: FieldDefaultScalar): FieldDefaultScalar = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      in.readString(null)
    } else if (b == 't' || b == 'f') {
      in.rollbackToken()
      in.readBoolean()
    } else if (b >= '0' && b <= '9') {
      in.rollbackToken()
      in.readInt()
    } else in.decodeError("expected string, boolean or int")
  }

  override def encodeValue(x: FieldDefaultScalar, out: JsonWriter): Unit = x match {
    case str: String => out.writeVal(str)
    case b: Boolean => out.writeVal(b)
    case i: Int => out.writeVal(i)
  }

  override def nullValue: FieldDefaultScalar = null
end given