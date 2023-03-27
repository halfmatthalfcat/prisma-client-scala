package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.{CodecMakerConfig, JsonCodecMaker}

sealed abstract class RPCResponse[T: JsonValueCodec](
  jsonrpc: String,
  id: Int,
  result: T
)
object RPCResponse:
  given JsonValueCodec[RPCResponse[?]] = JsonCodecMaker.make(
    CodecMakerConfig
      .withTransientDefault(false)
      .withTransientEmpty(false)
  )
end RPCResponse

case class GeneratorConfigResponse(
  id: Int,
  result: GeneratorManifestResponse,
  jsonrpc: String = "2.0",
) extends RPCResponse(id = id, result = result, jsonrpc = jsonrpc)


