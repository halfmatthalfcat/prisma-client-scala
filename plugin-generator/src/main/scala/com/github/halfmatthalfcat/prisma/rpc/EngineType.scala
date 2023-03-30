package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

object EngineType extends Enumeration {
  private type EngineType = Value
  
  protected case class EngineTypeVal(engine: String) extends super.Val
  implicit def valueToEngineTypeVal(x: Value): EngineTypeVal = x.asInstanceOf[EngineTypeVal]
  implicit val codec: JsonValueCodec[EngineType] = new JsonValueCodec[EngineType] {
    override def decodeValue(in: JsonReader, default: EngineType): EngineType = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        EngineType.values.find(_.engine == str).getOrElse(
          in.enumValueError("expected EngineType")
        )
      } else in.decodeError("expected EngineType")
    }

    override def encodeValue(x: EngineType, out: JsonWriter): Unit =
      out.writeVal(x.engine)

    override def nullValue: EngineType = null.asInstanceOf[EngineType]
  }
    
  val Query: EngineTypeVal = EngineTypeVal("queryEngine")
  val LibQuery: EngineTypeVal = EngineTypeVal("libqueryEngine")
  val Migration: EngineTypeVal = EngineTypeVal("migrationEngine")
}
