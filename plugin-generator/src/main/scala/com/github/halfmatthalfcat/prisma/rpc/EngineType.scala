package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

enum EngineType(val engine: String):
  case Query extends EngineType("queryEngine")
  case LibQuery extends EngineType("libqueryEngine")
  case Migration extends EngineType("migrationEngine")
end EngineType

given JsonValueCodec[EngineType] = new JsonValueCodec[EngineType]:
  override def decodeValue(in: JsonReader, default: EngineType): EngineType = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      val str = in.readString(null)
      EngineType.values.find(_.engine == str).getOrElse(
        in.decodeError("expected EngineType")
      )
    } else in.decodeError("expected engineType")
  }

  override def encodeValue(x: EngineType, out: JsonWriter): Unit =
    out.writeVal(x.engine)

  override def nullValue: EngineType = null
end given
