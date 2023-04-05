package com.github.halfmatthalfcat.prisma.scala

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

import scala.collection.mutable

sealed trait Json
object Json {
  implicit val codec: JsonValueCodec[Json] = new JsonValueCodec[Json] {
    override def decodeValue(in: JsonReader, default: Json): Json = {
      val b = in.nextToken()
      if (b == '{') {
        in.rollbackToken()
        val map: mutable.Map[String, Json] = mutable.Map.empty
        while (!in.isNextToken('}')) {
          val key = in.readKeyAsString()
          val value = decodeValue(in, null)
          map.addOne(key -> value)
        }
        JsonObject(map.toMap)
      } else if (b == '[') {
        in.rollbackToken()
        val seq: mutable.Buffer[Json] = mutable.Buffer.empty
        while (!in.isNextToken(']')) {
          seq += decodeValue(in, null)
        }
        JsonArray(seq.toSeq)
      } else if (b == '"') {
        in.rollbackToken()
        JsonString(in.readString(null))
      } else if (b == 't' || b == 'f') {
        in.rollbackToken()
        JsonBool(in.readBoolean())
      } else if (b >= '0' && b <= '9') {
        in.rollbackToken()
        JsonNumber(in.readInt())
      } else if (b == 'n') {
        in.readNullOrError(JsonNull, "expected null")
      } else {
        in.decodeError("Could not decode JSON value")
      }
    }

    override def encodeValue(x: Json, out: JsonWriter): Unit = x match {
      case JsonNumber(number) => out.writeVal(number)
      case JsonString(str) => out.writeVal(str)
      case JsonBool(bool) => out.writeVal(bool)
      case JsonObject(elements) =>
        out.writeObjectStart()
        for ((k, v) <- elements) {
          out.writeKey(k)
          encodeValue(v, out)
        }
        out.writeObjectEnd()
      case JsonArray(elements) =>
        out.writeArrayStart()
        for (e <- elements) {
          encodeValue(e, out)
        }
        out.writeArrayEnd()
      case JsonNull =>
        out.writeNull()
    }

    override def nullValue: Json = null.asInstanceOf[Json]
  }
}

case class JsonObject(elements: Map[String, Json]) extends Json

case class JsonArray(elements: Seq[Json]) extends Json

case class JsonNumber(number: Int) extends Json
object JsonNumber {
  implicit def asInt(jn: JsonNumber): Int = jn.number
}

case class JsonString(str: String) extends Json
object JsonString {
  implicit def asStr(js: JsonString): String = js.str
}

case class JsonBool(bool: Boolean) extends Json
object JsonBool {
  implicit def asBool(jb: JsonBool): Boolean = jb.bool
}

case object JsonNull extends Json