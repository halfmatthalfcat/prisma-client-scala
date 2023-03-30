package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class DataSource(
  name: String,
  provider: ConnectorType.Value,
  activeProvider: ConnectorType.Value,
  url: EnvValue,
  directUrl: Option[EnvValue],
  schemas: Seq[String]
)
object DataSource {
  implicit val codec: JsonValueCodec[DataSource] = JsonCodecMaker.make
}
