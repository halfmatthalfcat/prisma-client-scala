package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Document(
  datamodel: Datamodel,
  schema: Schema,
  mappings: Mappings,
)
object Document:
  given JsonValueCodec[Document] = JsonCodecMaker.make
end Document
