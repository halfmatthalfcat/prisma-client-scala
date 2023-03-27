package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

import scala.collection.mutable

type FieldDefaultValue = FieldDefault | FieldDefaultScalar | Seq[FieldDefaultScalar]
given JsonValueCodec[FieldDefaultValue] = new JsonValueCodec[FieldDefaultValue]:
  override def decodeValue(in: JsonReader, default: FieldDefaultValue): FieldDefaultValue = {
    val b = in.nextToken()
    if (b == '{') {
      in.rollbackToken()
      summon[JsonValueCodec[FieldDefault]].decodeValue(in, null)
    } else if (b == '[') {
      val arr: mutable.ArrayBuffer[FieldDefaultScalar] = mutable.ArrayBuffer.empty
      in.rollbackToken()
      in.arrayStartOrNullError()
      while ({
        arr += summon[JsonValueCodec[FieldDefaultScalar]].decodeValue(in, null)
        in.isNextToken(',')
      }) ()
      if (!in.isCurrentToken(']')) in.arrayEndOrCommaError()
      arr.toSeq
    } else {
      in.rollbackToken()
      summon[JsonValueCodec[FieldDefaultScalar]].decodeValue(in, null)
    }
  }

  override def encodeValue(x: FieldDefaultValue, out: JsonWriter): Unit = x match {
    case sfds: Seq[FieldDefaultScalar] =>
      val codec = summon[JsonValueCodec[FieldDefaultScalar]]
      out.writeArrayStart()
      sfds.foreach(fds => codec.encodeValue(fds, out))
      out.writeArrayEnd()
    case fds: FieldDefaultScalar => summon[JsonValueCodec[FieldDefaultScalar]].encodeValue(fds, out)
    case fd: FieldDefault => summon[JsonValueCodec[FieldDefault]].encodeValue(fd, out)
  }

  override def nullValue: FieldDefaultValue = null
end given