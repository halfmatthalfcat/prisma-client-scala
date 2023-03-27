package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class OtherOperationMappings(
  read: Seq[String],
  write: Seq[String],
)
object OtherOperationMappings:
  given JsonValueCodec[OtherOperationMappings] = JsonCodecMaker.make
end OtherOperationMappings

