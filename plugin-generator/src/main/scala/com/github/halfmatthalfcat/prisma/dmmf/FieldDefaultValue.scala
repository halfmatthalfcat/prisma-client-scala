package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

import scala.collection.mutable

sealed trait FieldDefaultValue
object FieldDefaultValue {
  implicit val codec: JsonValueCodec[FieldDefaultValue] = new JsonValueCodec[FieldDefaultValue] {
    override def decodeValue(in: JsonReader, default: FieldDefaultValue): FieldDefaultValue = {
      val b = in.nextToken()
      if (b == '{') {
        in.rollbackToken()
        IsFieldDefault(implicitly[JsonValueCodec[FieldDefault]].decodeValue(in, null))
      } else if (b == '[') {
        val arr: mutable.ArrayBuffer[FieldDefaultScalar] = mutable.ArrayBuffer.empty
        in.rollbackToken()
        in.arrayStartOrNullError()
        while ( {
          arr += implicitly[JsonValueCodec[FieldDefaultScalar]].decodeValue(in, null)
          in.isNextToken(',')
        }) ()
        if (!in.isCurrentToken(']')) in.arrayEndOrCommaError()
        IsFieldDefaultScalarSeq(arr.toSeq)
      } else {
        in.rollbackToken()
        IsFieldDefaultScalar(implicitly[JsonValueCodec[FieldDefaultScalar]].decodeValue(in, null))
      }
    }

    override def encodeValue(x: FieldDefaultValue, out: JsonWriter): Unit = x match {
      case IsFieldDefaultScalarSeq(fdss) =>
        val codec = implicitly[JsonValueCodec[FieldDefaultScalar]]
        out.writeArrayStart()
        fdss.foreach(fds => codec.encodeValue(fds, out))
        out.writeArrayEnd()
      case IsFieldDefaultScalar(fds) => implicitly[JsonValueCodec[FieldDefaultScalar]].encodeValue(fds, out)
      case IsFieldDefault(fd) => implicitly[JsonValueCodec[FieldDefault]].encodeValue(fd, out)
    }

    override def nullValue: FieldDefaultValue = null.asInstanceOf[FieldDefaultValue]
  }
}

case class IsFieldDefault(fd: FieldDefault) extends FieldDefaultValue
object IsFieldDefault {
  implicit def asFd(isFieldDefault: IsFieldDefault): FieldDefault = isFieldDefault.fd
  implicit def box(fd: FieldDefault): IsFieldDefault = IsFieldDefault(fd)
}

case class IsFieldDefaultScalar(fds: FieldDefaultScalar) extends FieldDefaultValue
object IsFieldDefaultScalar {
  implicit def asFds(isFieldDefaultScalar: IsFieldDefaultScalar): FieldDefaultScalar = isFieldDefaultScalar.fds
  implicit def box(fds: FieldDefaultScalar): IsFieldDefaultScalar = IsFieldDefaultScalar(fds)
}
case class IsFieldDefaultScalarSeq(fdss: Seq[FieldDefaultScalar]) extends FieldDefaultValue
object IsFieldDefaultScalarSeq {
  implicit def asFdss(isFieldDefaultScalarSeq: IsFieldDefaultScalarSeq): Seq[FieldDefaultScalar] = isFieldDefaultScalarSeq.fdss
  implicit def box(fdss: Seq[FieldDefaultScalar]): IsFieldDefaultScalarSeq = IsFieldDefaultScalarSeq(fdss)
}
