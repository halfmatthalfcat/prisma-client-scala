package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.meta._

case class DatamodelEnum(
  name: String,
  values: Seq[EnumValue],
  dbName: Option[String],
  documentation: String,
)

object DatamodelEnum {
  implicit val codec: JsonValueCodec[DatamodelEnum] = JsonCodecMaker.make
}