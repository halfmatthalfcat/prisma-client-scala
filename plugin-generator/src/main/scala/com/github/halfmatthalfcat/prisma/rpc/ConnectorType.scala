package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed abstract class ConnectorType(val connector: String)
object ConnectorType {
  private val values = Seq(
    Mysql,
    Mongo,
    Sqlite,
    Postgres,
    Postgresql,
    SqlServer,
    Cockroach,
  )

  case object Mysql extends ConnectorType("mysql")
  case object Mongo extends ConnectorType("mongodb")
  case object Sqlite extends ConnectorType("sqlite")
  case object Postgres extends ConnectorType("postgres")
  case object Postgresql extends ConnectorType("postgresql")
  case object SqlServer extends ConnectorType("sqlserver")
  case object Cockroach extends ConnectorType("cockroachdb")

  implicit val codec: JsonValueCodec[ConnectorType] = new JsonValueCodec[ConnectorType] {
    override def decodeValue(in: JsonReader, default: ConnectorType): ConnectorType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.connector == str).getOrElse(
          in.enumValueError("expected ConnectorType")
        )
      } else in.decodeError("expected ConnectorType")
    }

    override def encodeValue(x: ConnectorType, out: JsonWriter): Unit =
      out.writeVal(x.connector)

    override def nullValue: ConnectorType = null.asInstanceOf[ConnectorType]
  }
}
