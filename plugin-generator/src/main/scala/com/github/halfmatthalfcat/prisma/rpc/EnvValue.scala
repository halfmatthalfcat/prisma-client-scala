package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class EnvValue(
  fromEnvVar: Option[String],
  value: Option[String],
)
object EnvValue:
  given JsonValueCodec[EnvValue] = JsonCodecMaker.make
end EnvValue
