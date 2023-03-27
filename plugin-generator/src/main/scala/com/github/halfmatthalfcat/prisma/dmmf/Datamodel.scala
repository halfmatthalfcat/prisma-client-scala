package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Datamodel(
  models: Seq[Model],
  enums: Seq[DatamodelEnum],
  types: Seq[Model],
)
object Datamodel:
  given JsonValueCodec[Datamodel] = JsonCodecMaker.make
end Datamodel
