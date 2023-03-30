package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed trait FieldDefaultScalar
object FieldDefaultScalar {
  implicit val codec: JsonValueCodec[FieldDefaultScalar] = new JsonValueCodec[FieldDefaultScalar] {
    override def decodeValue(in: JsonReader, default: FieldDefaultScalar): FieldDefaultScalar = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        IsFieldDefaultScalarString(in.readString(null))
      } else if (b == 't' || b == 'f') {
        in.rollbackToken()
        IsFieldDefaultScalarBoolean(in.readBoolean())
      } else if (b >= '0' && b <= '9') {
        in.rollbackToken()
        IsFieldDefaultScalarInt(in.readInt())
      } else in.decodeError("expected string, boolean or int")
    }

    override def encodeValue(x: FieldDefaultScalar, out: JsonWriter): Unit = x match {
      case IsFieldDefaultScalarString(str) => out.writeVal(str)
      case IsFieldDefaultScalarBoolean(b) => out.writeVal(b)
      case IsFieldDefaultScalarInt(i) => out.writeVal(i)
    }

    override def nullValue: FieldDefaultScalar = null.asInstanceOf[FieldDefaultScalar]
  }
}

case class IsFieldDefaultScalarString(str: String) extends FieldDefaultScalar
object IsFieldDefaultScalarString {
  implicit def asStr(isString: IsFieldDefaultScalarString): String = isString.str
  implicit def box(str: String): IsFieldDefaultScalarString = IsFieldDefaultScalarString(str)
}

case class IsFieldDefaultScalarBoolean(b: Boolean) extends FieldDefaultScalar
object IsFieldDefaultScalarBoolean {
  implicit def asBool(isBoolean: IsFieldDefaultScalarBoolean): Boolean = isBoolean.b
  implicit def box(b: Boolean): IsFieldDefaultScalarBoolean = IsFieldDefaultScalarBoolean(b)
}

case class IsFieldDefaultScalarInt(i: Int) extends FieldDefaultScalar
object IsFieldDefaultScalarInt {
  implicit def asInt(isInt: IsFieldDefaultScalarInt): Int = isInt.i
  implicit def box(i: Int): IsFieldDefaultScalarInt = IsFieldDefaultScalarInt(i)
}
