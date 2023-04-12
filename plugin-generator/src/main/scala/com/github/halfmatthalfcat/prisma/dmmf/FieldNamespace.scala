package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed abstract class FieldNamespace(val namespace: String)
object FieldNamespace {
  private val values = Seq(
    Model,
    Prisma,
  )

  case object Model extends FieldNamespace("model")
  case object Prisma extends FieldNamespace("prisma")

  implicit val codec: JsonValueCodec[FieldNamespace] = new JsonValueCodec[FieldNamespace] {
    override def decodeValue(in: JsonReader, default: FieldNamespace): FieldNamespace = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.namespace == str).getOrElse(
          in.enumValueError("expected FieldNamespace")
        )
      } else in.decodeError("expected FieldNamespace")
    }

    override def encodeValue(x: FieldNamespace, out: JsonWriter): Unit =
      out.writeVal(x.namespace)

    override def nullValue: FieldNamespace = null.asInstanceOf[FieldNamespace]
  }
}

