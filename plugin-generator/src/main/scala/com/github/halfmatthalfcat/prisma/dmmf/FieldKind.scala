package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

object FieldKind extends Enumeration {
  type FieldKind = Value

  protected case class FieldKindVal(kind: String) extends super.Val

  val Scalar: FieldKindVal = FieldKindVal("scalar")
  val Object: FieldKindVal = FieldKindVal("object")
  val Enum: FieldKindVal = FieldKindVal("enum")
  val Unsupported: FieldKindVal = FieldKindVal("unsupported")

  implicit def valueToFieldKindVal(x: Value): FieldKindVal = x.asInstanceOf[FieldKindVal]

  implicit val codec: JsonValueCodec[FieldKind] = new JsonValueCodec[FieldKind] {
    override def decodeValue(in: JsonReader, default: FieldKind): FieldKind = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        FieldKind.values.find(_.kind == str).getOrElse(
          in.enumValueError("expected FieldKind")
        )
      } else in.decodeError("expected FieldKind")
    }

    override def encodeValue(x: FieldKind, out: JsonWriter): Unit =
      out.writeVal(x.kind)

    override def nullValue: FieldKind = null.asInstanceOf[FieldKind]
  }
}
