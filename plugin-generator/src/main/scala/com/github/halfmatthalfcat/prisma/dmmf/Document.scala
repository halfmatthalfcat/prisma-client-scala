package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Document(
  datamodel: Datamodel,
  schema: Schema,
  mappings: Mappings,
)

object Document {
  implicit val codec: JsonValueCodec[Document] = JsonCodecMaker.make
}
