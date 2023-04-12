package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed abstract class FieldKind(val kind: String)
object FieldKind {
  private val values = Seq(
    Scalar,
    Object,
    Enum,
    Unsupported,
  )

  case object Scalar extends FieldKind("scalar")
  case object Object extends FieldKind("object")
  case object Enum extends FieldKind("enum")
  case object Unsupported extends FieldKind("unsupported")

  implicit val codec: JsonValueCodec[FieldKind] = new JsonValueCodec[FieldKind] {
    override def decodeValue(in: JsonReader, default: FieldKind): FieldKind = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.kind == str).getOrElse(
          in.enumValueError("expected FieldKind")
        )
      } else in.decodeError("expected FieldKind")
    }

    override def encodeValue(x: FieldKind, out: JsonWriter): Unit =
      out.writeVal(x.kind)

    override def nullValue: FieldKind = null.asInstanceOf[FieldKind]
  }
}
