package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

enum FieldNamespace(val namespace: String):
  case Model extends FieldNamespace("model")
  case Prisma extends FieldNamespace("prisma")
end FieldNamespace

given JsonValueCodec[FieldNamespace] = new JsonValueCodec[FieldNamespace]:
  override def decodeValue(in: JsonReader, default: FieldNamespace): FieldNamespace = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      val str = in.readString(null)
      FieldNamespace.values.find(_.namespace == str).getOrElse(
        in.decodeError("expected FieldNamespace")
      )
    } else in.decodeError("expected FieldNamespace")
  }

  override def encodeValue(x: FieldNamespace, out: JsonWriter): Unit =
    out.writeVal(x.namespace)

  override def nullValue: FieldNamespace = null
end given
