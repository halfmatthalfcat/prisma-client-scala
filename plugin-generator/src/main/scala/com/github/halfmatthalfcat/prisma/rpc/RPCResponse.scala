package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros._

case class GeneratorConfigResponse(
  id: Int,
  result: GeneratorManifestResponse,
  jsonrpc: String = "2.0",
)
object GeneratorConfigResponse {
  implicit val codec: JsonValueCodec[GeneratorConfigResponse] = JsonCodecMaker.make(
    CodecMakerConfig
      .withTransientDefault(false)
      .withTransientEmpty(false)
  )
}


