package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed trait TypeKind
object TypeKind {
  private val values = Seq(
    String,
    Int,
    Float,
    Boolean,
    Long,
    DateTime,
    ID,
    UUID,
    Json,
    Bytes,
    Decimal,
    BigInt
  )
  case object String extends TypeKind
  case object Int extends TypeKind
  case object Float extends TypeKind
  case object Boolean extends TypeKind
  case object Long extends TypeKind
  case object DateTime extends TypeKind
  case object ID extends TypeKind
  case object UUID extends TypeKind
  case object Json extends TypeKind
  case object Bytes extends TypeKind
  case object Decimal extends TypeKind
  case object BigInt extends TypeKind
  case class Other(kind: String) extends TypeKind

  implicit val codec: JsonValueCodec[TypeKind] = new JsonValueCodec[TypeKind] {
    override def decodeValue(in: JsonReader, default: TypeKind): TypeKind = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.toString == str).getOrElse(Other(str))
      } else in.decodeError("expected TypeKind")
    }

    override def encodeValue(x: TypeKind, out: JsonWriter): Unit = x match {
      case Other(kind) => out.writeVal(kind)
      case _ => out.writeVal(x.toString)
    }

    override def nullValue: TypeKind = null.asInstanceOf[TypeKind]
  }
}
