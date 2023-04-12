package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class GeneratorManifestResponse(
  manifest: GeneratorManifest
)
object GeneratorManifestResponse {
  implicit val codec: JsonValueCodec[GeneratorManifestResponse] = JsonCodecMaker.make
}

case class GeneratorManifest(
  prettyName: String,
  version: String,
  defaultOutput: Option[String] = None,
  denylists: Option[GeneratorDenyLists] = None,
  requiresGenerators: Option[Seq[String]] = None,
  requiresEngines: Option[Seq[EngineType.type]] = None,
  requresEngineVersion: Option[String] = None,
) {
  def asResponse: GeneratorManifestResponse = 
    GeneratorManifestResponse(this)
}
object GeneratorManifest {
  implicit val codec: JsonValueCodec[GeneratorManifest] = JsonCodecMaker.make
}
