package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

// @see https://github.com/prisma/prisma/blob/4.11.0/packages/generator-helper/src/dmmf.ts#L219
case class ModelMapping(
  model: String,
  plural: Option[String],
  findUnique: Option[String],
  findUniqueOrThrow: Option[String],
  findFirst: Option[String],
  findFirstOrThrow: Option[String],
  findMany: Option[String],
  create: Option[String],
  createMany: Option[String],
  update: Option[String],
  updateMany: Option[String],
  upsert: Option[String],
  delete: Option[String],
  deleteMany: Option[String],
  aggregate: Option[String],
  groupBy: Option[String],
  count: Option[String],
  findRaw: Option[String],
  aggregateRaw: Option[String],
)
object ModelMapping:
  given JsonValueCodec[ModelMapping] = JsonCodecMaker.make
end ModelMapping
