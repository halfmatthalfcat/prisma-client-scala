package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed abstract class FieldLocation(val location: String)
object FieldLocation {
  private val values = Seq(
    Scalar,
    InputObjectTypes,
    OutputObjectTypes,
    EnumTypes,
    FieldRefTypes,
  )

  case object Scalar extends FieldLocation("scalar")
  case object InputObjectTypes extends FieldLocation("inputObjectTypes")
  case object OutputObjectTypes extends FieldLocation("outputObjectTypes")
  case object EnumTypes extends FieldLocation("enumTypes")
  case object FieldRefTypes extends FieldLocation("fieldRefTypes")

  implicit val codec: JsonValueCodec[FieldLocation] = new JsonValueCodec[FieldLocation] {
    override def decodeValue(in: JsonReader, default: FieldLocation): FieldLocation = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.location == str).getOrElse(
          in.enumValueError("expected FieldLocation")
        )
      } else in.decodeError("expected FieldLocation")
    }

    override def encodeValue(x: FieldLocation, out: JsonWriter): Unit =
      out.writeVal(x.location)

    override def nullValue: FieldLocation = null.asInstanceOf[FieldLocation]
  }
}
