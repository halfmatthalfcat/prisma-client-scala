package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class DataSource(
  name: String,
  provider: ConnectorType,
  activeProvider: ConnectorType,
  url: EnvValue,
  directUrl: Option[EnvValue],
  schemas: Seq[String]
)
object DataSource:
  given JsonValueCodec[DataSource] = JsonCodecMaker.make
end DataSource
