package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class OutputObjectTypes(
  model: Seq[OutputType],
  prisma: Seq[OutputType],
)
object OutputObjectTypes {
  implicit val codec: JsonValueCodec[OutputObjectTypes] = JsonCodecMaker.make
}
