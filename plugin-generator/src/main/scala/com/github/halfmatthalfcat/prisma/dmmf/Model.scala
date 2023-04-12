package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Model(
  name: String,
  dbName: Option[String] = None,
  fields: Seq[Field],
  uniqueFields: Seq[Seq[String]] = Seq.empty,
  uniqueIndexes: Seq[UniqueIndex] = Seq.empty,
  documentation: Option[String] = None,
  primaryKey: Option[PrimaryKey] = None,
)
object Model {
  implicit val codec: JsonValueCodec[Model] = JsonCodecMaker.make
}

