package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

// @see https://github.com/prisma/prisma/blob/4.11.0/packages/generator-helper/src/dmmf.ts#L241
enum ModelAction(val action: String):
  case FindUnique extends ModelAction("findUnique")
  case FindUniqueOrThrow extends ModelAction("findUniqueOrThrow")
  case FindFirst extends ModelAction("findFirst")
  case FindFirstOrThrow extends ModelAction("findFirstOrThrow")
  case FindMany extends ModelAction("findMany")
  case Create extends ModelAction("create")
  case CreateMany extends ModelAction("createMany")
  case Update extends ModelAction("update")
  case UpdateMany extends ModelAction("updateMany")
  case Upsert extends ModelAction("upsert")
  case Delete extends ModelAction("delete")
  case DeleteMany extends ModelAction("deleteMany")
  case GroupBy extends ModelAction("groupBy")
  case Count extends ModelAction("count")
  case Aggregate extends ModelAction("aggregate")
  case FindRaw extends ModelAction("findRaw")
  case AggregateRaw extends ModelAction("aggregateRaw")
end ModelAction

given JsonValueCodec[ModelAction] = new JsonValueCodec[ModelAction]:
  override def decodeValue(in: JsonReader, default: ModelAction): ModelAction = {
    val b = in.nextToken()
    if (b == '"') {
      in.rollbackToken()
      val str = in.readString(null)
      ModelAction.values.find(_.action == str).getOrElse(
        in.decodeError("expected ModelAction")
      )
    } else in.decodeError("expected ModelAction")
  }

  override def encodeValue(x: ModelAction, out: JsonWriter): Unit =
    out.writeVal(x.action)

  override def nullValue: ModelAction = null
end given
