package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

enum ConnectorType(val connector: String):
  case Mysql extends ConnectorType("mysql")
  case Mongo extends ConnectorType("mongodb")
  case Sqlite extends ConnectorType("sqlite")
  case Postgres extends ConnectorType("postgres")
  case Postgresql extends ConnectorType("postgresql")
  case SqlServer extends ConnectorType("sqlserver")
  case CockroachDB extends ConnectorType("cockroachdb")
end ConnectorType

given JsonValueCodec[ConnectorType] = new JsonValueCodec[ConnectorType]:
  override def decodeValue(in: JsonReader, default: ConnectorType): ConnectorType = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      val str = in.readString(null)
      ConnectorType.values.find(_.connector == str).getOrElse(
        in.decodeError("expected ConnectorType")
      )
    } else in.decodeError("expected ConnectorType")
  }

  override def encodeValue(x: ConnectorType, out: JsonWriter): Unit =
    out.writeVal(x.connector)

  override def nullValue: ConnectorType = null
end given
