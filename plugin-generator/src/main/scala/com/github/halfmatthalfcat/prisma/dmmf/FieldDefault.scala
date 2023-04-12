package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{JsonCodecMaker, named}

case class FieldDefault(
  @named("name") kind: FieldDefaultKind,
  args: Seq[FieldDefaultScalar]
)

object FieldDefault {
  implicit val codec: JsonValueCodec[FieldDefault] = JsonCodecMaker.make
}
