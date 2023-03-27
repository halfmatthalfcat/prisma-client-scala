package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class Mappings(
  modelOperations: Seq[ModelMapping],
  otherOperations: OtherOperationMappings,
)
object Mappings:
  given JsonValueCodec[Mappings] = JsonCodecMaker.make
end Mappings

