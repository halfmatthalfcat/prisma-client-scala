package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class EnumTypes(
  model: Option[Seq[SchemaEnum]],
  prisma: Seq[SchemaEnum],
)
object EnumTypes:
  given JsonValueCodec[EnumTypes] = JsonCodecMaker.make
end EnumTypes
