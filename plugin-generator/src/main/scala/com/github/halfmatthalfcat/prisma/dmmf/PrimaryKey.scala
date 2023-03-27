package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class PrimaryKey(
  name: Option[String],
  fields: Seq[String],
)
object PrimaryKey:
  given JsonValueCodec[PrimaryKey] = JsonCodecMaker.make
end PrimaryKey
