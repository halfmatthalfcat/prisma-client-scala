package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class QueryOutput(
  name: String,
  isRequired: Boolean,
  isList: Boolean,
)
object QueryOutput:
  given JsonValueCodec[QueryOutput] = JsonCodecMaker.make
end QueryOutput
