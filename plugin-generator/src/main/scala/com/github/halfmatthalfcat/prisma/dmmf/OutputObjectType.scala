package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed trait OutputObjectType
object OutputObjectType {
  implicit val codec: JsonValueCodec[OutputObjectType] = new JsonValueCodec[OutputObjectType] {
    override def decodeValue(in: JsonReader, default: OutputObjectType): OutputObjectType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        IsOutputObjectTypeString(in.readString(null))
      } else if (b == '{') {
        in.rollbackToken()
        IsOutputObjectTypeOutputType(
          implicitly[JsonValueCodec[OutputType]].decodeValue(in, null)
        )
      } else in.decodeError("expected String or OutputType")
    }

    override def encodeValue(x: OutputObjectType, out: JsonWriter): Unit = x match {
      case IsOutputObjectTypeString(str) => out.writeVal(str)
      case IsOutputObjectTypeOutputType(outputType) => 
        implicitly[JsonValueCodec[OutputType]].encodeValue(outputType, null)
    }

    override def nullValue: OutputObjectType = null.asInstanceOf[OutputObjectType]
  }
}

case class IsOutputObjectTypeString(str: String) extends OutputObjectType
object IsOutputObjectTypeString {
  implicit def asStr(isOutputObjectTypeString: IsOutputObjectTypeString): String =
    isOutputObjectTypeString.str
  implicit def box(str: String): IsOutputObjectTypeString = IsOutputObjectTypeString(str)
}

case class IsOutputObjectTypeOutputType(outputType: OutputType) extends OutputObjectType
object IsOutputObjectTypeOutputType {
  implicit def asOutputType(isOutputObjectTypeOutputType: IsOutputObjectTypeOutputType): OutputType =
    isOutputObjectTypeOutputType.outputType
  implicit def box(outputType: OutputType): IsOutputObjectTypeOutputType = 
    IsOutputObjectTypeOutputType(outputType)
}