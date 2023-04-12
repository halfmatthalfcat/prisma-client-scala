package com.github.halfmatthalfcat.prisma.dmmf

import com.github.halfmatthalfcat.prisma.dmmf.FieldKind.values
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed abstract class FieldDefaultKind(val kind: String)
object FieldDefaultKind {
  private val values = Seq(
    Autoincrement,
    Sequence,
    DbGenerated,
    CUID,
    UUID,
    Now,
  )

  case object Autoincrement extends FieldDefaultKind("autoincrement")
  case object Sequence extends FieldDefaultKind("sequence")
  case object DbGenerated extends FieldDefaultKind("dbgenerated")
  case object CUID extends FieldDefaultKind("cuid")
  case object UUID extends FieldDefaultKind("uuid")
  case object Now extends FieldDefaultKind("now")

  implicit val codec: JsonValueCodec[FieldDefaultKind] = new JsonValueCodec[FieldDefaultKind] {
    override def decodeValue(in: JsonReader, default: FieldDefaultKind): FieldDefaultKind = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.kind == str).getOrElse(
          in.enumValueError("expected FieldDefaultKind")
        )
      } else in.decodeError("expected FieldDefaultKind")
    }

    override def encodeValue(x: FieldDefaultKind, out: JsonWriter): Unit =
      out.writeVal(x.kind)

    override def nullValue: FieldDefaultKind = null.asInstanceOf[FieldDefaultKind]
  }
}
