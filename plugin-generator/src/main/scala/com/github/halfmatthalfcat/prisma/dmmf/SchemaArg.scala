package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class SchemaArg(
  name: String,
  comment: Option[String],
  isNullable: Boolean,
  isRequired: Boolean,
  inputTypes: Seq[SchemaArgInputType],
  deprecation: Option[Deprecation]
)
object SchemaArg:
  given JsonValueCodec[SchemaArg] = JsonCodecMaker.make
end SchemaArg
