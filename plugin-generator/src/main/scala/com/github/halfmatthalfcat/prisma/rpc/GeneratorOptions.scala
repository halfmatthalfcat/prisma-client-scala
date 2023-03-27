package com.github.halfmatthalfcat.prisma.rpc

import com.github.halfmatthalfcat.prisma.dmmf.Document
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

case class GeneratorOptions(
  generator: GeneratorConfig,
  otherGenerators: Seq[GeneratorConfig],
  schemaPath: String,
  dmmf: Document,
  datasources: Seq[DataSource],
  datamodel: String,
  version: String,
  binaryPaths: Option[BinaryPaths],
  dataProxy: Boolean,
)
object GeneratorOptions:
  given JsonValueCodec[GeneratorOptions] = JsonCodecMaker.make
end GeneratorOptions
