package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

object ModelAction extends Enumeration {
  private type ModelAction = Value
  
  protected case class ModelActionVal(action: String) extends super.Val
  implicit def valueToModelActionVal(x: Value): ModelActionVal = x.asInstanceOf[ModelActionVal]
  implicit val codec: JsonValueCodec[ModelAction] = new JsonValueCodec[ModelAction] {
    override def decodeValue(in: JsonReader, default: ModelAction): ModelAction = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        ModelAction.values.find(_.action == str).getOrElse(
          in.enumValueError("expected ModelAction")
        )
      } else in.decodeError("expected ModelAction")
    }

    override def encodeValue(x: ModelAction, out: JsonWriter): Unit =
      out.writeVal(x.action)

    override def nullValue: ModelAction = null.asInstanceOf[ModelAction]
  }
  
  val FindUnique: ModelActionVal = ModelActionVal("findUnique")
  val FindUniqueOrThrow: ModelActionVal = ModelActionVal("findUniqueOrThrow")
  val FindFirst: ModelActionVal = ModelActionVal("findFirst")
  val FindFirstOrThrow: ModelActionVal = ModelActionVal("findFirstOrThrow")
  val FindMany: ModelActionVal = ModelActionVal("findMany")
  val Create: ModelActionVal = ModelActionVal("create")
  val CreateMany: ModelActionVal = ModelActionVal("createMany")
  val Update: ModelActionVal = ModelActionVal("update")
  val UpdateMany: ModelActionVal = ModelActionVal("updateMany")
  val Upsert: ModelActionVal = ModelActionVal("upsert")
  val Delete: ModelActionVal = ModelActionVal("delete")
  val DeleteMany: ModelActionVal = ModelActionVal("deleteMany")
  val GroupBy: ModelActionVal = ModelActionVal("groupBy")
  val Count: ModelActionVal = ModelActionVal("count")
  val Aggregate: ModelActionVal = ModelActionVal("aggregate")
  val FindRaw: ModelActionVal = ModelActionVal("findRaw")
  val AggregateRaw: ModelActionVal = ModelActionVal("aggregateRaw")
}
