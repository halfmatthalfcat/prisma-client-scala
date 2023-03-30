package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

object ConnectorType extends Enumeration {
  type ConnectorType = Value

  protected case class ConnectorTypeVal(connector: String) extends super.Val

  val Mysql: ConnectorTypeVal = ConnectorTypeVal("mysql")
  val Mongo: ConnectorTypeVal = ConnectorTypeVal("mongodb")
  val Sqlite: ConnectorTypeVal = ConnectorTypeVal("sqlite")
  val Postgres: ConnectorTypeVal = ConnectorTypeVal("postgres")
  val Postgresql: ConnectorTypeVal = ConnectorTypeVal("postgresql")
  val SqlServer: ConnectorTypeVal = ConnectorTypeVal("sqlserver")
  val Cockroach: ConnectorTypeVal = ConnectorTypeVal("cockroachdb")

  implicit def valueToConnectorTypeVal(x: Value): ConnectorTypeVal = x.asInstanceOf[ConnectorTypeVal]
  implicit val codec: JsonValueCodec[ConnectorType] = new JsonValueCodec[ConnectorType] {
    override def decodeValue(in: JsonReader, default: ConnectorType): ConnectorType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        ConnectorType.values.find(_.connector == str).getOrElse(
          in.enumValueError("expected ConnectorType")
        )
      } else in.decodeError("expected ConnectorType")
    }

    override def encodeValue(x: ConnectorType, out: JsonWriter): Unit =
      out.writeVal(x.connector)

    override def nullValue: ConnectorType = null.asInstanceOf[ConnectorType]
  }
}
