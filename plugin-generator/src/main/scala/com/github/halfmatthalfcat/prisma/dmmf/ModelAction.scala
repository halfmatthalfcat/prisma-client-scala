package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

sealed abstract class ModelAction(val action: String)
object ModelAction {
  private val values = Seq(
    FindUnique,
    FindUniqueOrThrow,
    FindFirst,
    FindFirstOrThrow,
    FindMany,
    Create,
    CreateMany,
    Update,
    UpdateMany,
    Upsert,
    Delete,
    DeleteMany,
    GroupBy,
    Count,
    Aggregate,
    FindRaw,
    AggregateRaw,
  )

  case object FindUnique extends ModelAction("findUnique")
  case object FindUniqueOrThrow extends ModelAction("findUniqueOrThrow")
  case object FindFirst extends ModelAction("findFirst")
  case object FindFirstOrThrow extends ModelAction("findFirstOrThrow")
  case object FindMany extends ModelAction("findMany")
  case object Create extends ModelAction("create")
  case object CreateMany extends ModelAction("createMany")
  case object Update extends ModelAction("update")
  case object UpdateMany extends ModelAction("updateMany")
  case object Upsert extends ModelAction("upsert")
  case object Delete extends ModelAction("delete")
  case object DeleteMany extends ModelAction("deleteMany")
  case object GroupBy extends ModelAction("groupBy")
  case object Count extends ModelAction("count")
  case object Aggregate extends ModelAction("aggregate")
  case object FindRaw extends ModelAction("findRaw")
  case object AggregateRaw extends ModelAction("aggregateRaw")

  implicit val codec: JsonValueCodec[ModelAction] = new JsonValueCodec[ModelAction] {
    override def decodeValue(in: JsonReader, default: ModelAction): ModelAction = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        values.find(_.action == str).getOrElse(
          in.enumValueError("expected ModelAction")
        )
      } else in.decodeError("expected ModelAction")
    }

    override def encodeValue(x: ModelAction, out: JsonWriter): Unit =
      out.writeVal(x.action)

    override def nullValue: ModelAction = null.asInstanceOf[ModelAction]
  }
}
