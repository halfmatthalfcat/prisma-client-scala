package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class InputObjectTypes(
  model: Option[Seq[InputType]],
  prisma: Seq[InputType],
)
object InputObjectTypes {
  implicit val codec: JsonValueCodec[InputObjectTypes] = JsonCodecMaker.make
}
