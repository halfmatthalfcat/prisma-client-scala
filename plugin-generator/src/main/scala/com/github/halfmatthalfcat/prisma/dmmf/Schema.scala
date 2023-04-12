package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Schema(
  rootQueryType: Option[String],
  rootMutationType: Option[String],
  inputObjectTypes: InputObjectTypes,
  outputObjectTypes: OutputObjectTypes,
  enumTypes: EnumTypes,
  fieldRefTypes: FieldRefTypes,
)
object Schema {
  implicit val codec: JsonValueCodec[Schema] = JsonCodecMaker.make
}
