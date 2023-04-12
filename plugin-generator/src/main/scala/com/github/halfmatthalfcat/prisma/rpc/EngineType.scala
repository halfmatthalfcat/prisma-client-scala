package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

sealed abstract class EngineType(val engine: String)
object EngineType {
  private val values = Seq(
    Query,
    LibQuery,
    Migration,
  )

  case object Query extends EngineType("queryEngine")
  case object LibQuery extends EngineType("libqueryEngine")
  case object Migration extends EngineType("migrationEngine")

  implicit val codec: JsonValueCodec[EngineType] = new JsonValueCodec[EngineType] {
    override def decodeValue(in: JsonReader, default: EngineType): EngineType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.engine == str).getOrElse(
          in.enumValueError("expected EngineType")
        )
      } else in.decodeError("expected EngineType")
    }

    override def encodeValue(x: EngineType, out: JsonWriter): Unit =
      out.writeVal(x.engine)

    override def nullValue: EngineType = null.asInstanceOf[EngineType]
  }
}
