package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class GeneratorDenyLists(
  models: Option[Seq[String]] = None,
  fields: Option[Seq[String]] = None,
)
object GeneratorDenyLists {
  implicit val codec: JsonValueCodec[GeneratorDenyLists] = JsonCodecMaker.make
}
