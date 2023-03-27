package com.github.halfmatthalfcat.prisma.rpc

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class GeneratorManifestResponse(
  manifest: GeneratorManifest
)
object GeneratorManifestResponse:
  given JsonValueCodec[GeneratorManifestResponse] = JsonCodecMaker.make
end GeneratorManifestResponse

case class GeneratorManifest(
  prettyName: String,
  version: String,
  defaultOutput: Option[String] = None,
  denylists: Option[GeneratorDenyLists] = None,
  requiresGenerators: Option[Seq[String]] = None,
  requiresEngines: Option[Seq[EngineType]] = None,
  requresEngineVersion: Option[String] = None,
) {
  def asResponse: GeneratorManifestResponse = 
    GeneratorManifestResponse(this)
}
object GeneratorManifest:
  given JsonValueCodec[GeneratorManifest] = JsonCodecMaker.make
end GeneratorManifest
