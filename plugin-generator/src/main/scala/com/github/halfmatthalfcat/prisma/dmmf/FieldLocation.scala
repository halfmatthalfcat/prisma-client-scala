package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

enum FieldLocation(val location: String):
  case Scalar extends FieldLocation("scalar")
  case InputObjectTypes extends FieldLocation("inputObjectTypes")
  case OutputObjectTypes extends FieldLocation("outputObjectTypes")
  case EnumTypes extends FieldLocation("enumTypes")
  case FieldRefTypes extends FieldLocation("fieldRefTypes")
end FieldLocation

given JsonValueCodec[FieldLocation] = new JsonValueCodec[FieldLocation]:
  override def decodeValue(in: JsonReader, default: FieldLocation): FieldLocation = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      val str = in.readString(null)
      FieldLocation.values.find(_.location == str).getOrElse(
        in.decodeError("expected FieldLocation")
      )
    } else in.decodeError("expected FieldLocation")
  }

  override def encodeValue(x: FieldLocation, out: JsonWriter): Unit =
    out.writeVal(x.location)

  override def nullValue: FieldLocation = null
end given
