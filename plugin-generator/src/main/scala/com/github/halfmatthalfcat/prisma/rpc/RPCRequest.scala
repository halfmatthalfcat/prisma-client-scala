package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros._

sealed abstract class RPCRequest[T: JsonValueCodec](
  jsonrpc: String = "2.0",
  id: Int,
  params: T
)
object RPCRequest {
  implicit val codec: JsonValueCodec[RPCRequest[_]] = JsonCodecMaker.make(
    CodecMakerConfig
      .withDiscriminatorFieldName(Some("method"))
      .withRequireDiscriminatorFirst(false)
      .withTransientDefault(false)
      .withTransientEmpty(false)
  )
}

@named("getManifest") case class GeneratorConfigRequest(
  id: Int,
  params: GeneratorConfig,
) extends RPCRequest(id = id, params = params)

@named("generate") case class ManifestRequest(
  id: Int,
  params: GeneratorOptions,
) extends RPCRequest(id = id, params = params)