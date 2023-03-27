package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class GeneratorConfig(
  name: Option[String] = None,
  output: Option[EnvValue] = None,
  isCustomOutput: Option[Boolean] = None,
  provider: Option[EnvValue] = None,
  config: Option[Map[String, String]] = None,
  binaryTargets: Option[Seq[EnvValue]] = None,
  previewFeatures: Option[Seq[String]] = None
)
object GeneratorConfig:
  given JsonValueCodec[GeneratorConfig] = JsonCodecMaker.make
