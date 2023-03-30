package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Model(
  name: String,
  dbName: Option[String],
  fields: Seq[Field],
  uniqueFields: Seq[Seq[String]],
  uniqueIndexes: Seq[UniqueIndex],
  documentation: Option[String],
  primaryKey: Option[PrimaryKey],
)
object Model {
  implicit val codec: JsonValueCodec[Model] = JsonCodecMaker.make
}

