package com.github.halfmatthalfcat.util

import com.github.halfmatthalfcat.ScalaVersion
import os.Path

object Schema {
  def ensureGenerator(
    destPath: String
  ): Unit = {
    os.write.over(
      Path(s"$destPath/generator.jar"),
      Schema.getClass.getResourceAsStream("/generator.jar")
    )
  }

  def loadSchema(
    sourcePath: String,
    resourcePath: String,
    outPath: String,
    scalaVersion: ScalaVersion
  ): String = {
    val schema = os
      .read(Path(sourcePath))
      .replaceAll(
        "prisma-client-scala",
        s"java -jar $resourcePath/generator.jar -o $outPath -s ${scalaVersion.version}",
      )

    os.makeDir.all(Path(resourcePath))
    os.write.over(Path(s"$resourcePath/schema.prisma"), schema)
    s"$resourcePath/schema.prisma"
  }
}
