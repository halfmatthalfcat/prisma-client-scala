package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Query(
  name: String,
  args: Seq[SchemaArg],
  output: QueryOutput
)
object Query:
  given JsonValueCodec[Query] = JsonCodecMaker.make
end Query
