package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

enum FieldKind(val kind: String):
  case Scalar extends FieldKind("scalar")
  case Object extends FieldKind("object")
  case Enum extends FieldKind("enum")
  case Unsupported extends FieldKind("unsupported")
end FieldKind

given JsonValueCodec[FieldKind] = new JsonValueCodec[FieldKind]:
  override def decodeValue(in: JsonReader, default: FieldKind): FieldKind = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      val str = in.readString(null)
      FieldKind.values.find(_.kind == str).getOrElse(
        in.decodeError("expected FieldKind")
      )
    } else in.decodeError("expected FieldKind")
  }

  override def encodeValue(x: FieldKind, out: JsonWriter): Unit =
    out.writeVal(x.kind)

  override def nullValue: FieldKind = null
end given
