package com.github.halfmatthalfcat.config

import scopt.OParser

case class Config(
  output: String = "",
  scalaVersion: Int = 2,
)
object Config {
  private val builder = OParser.builder[Config]
  val argParser = {
    import builder._
    OParser.sequence(
      programName(buildinfo.BuildInfo.name),
      head(buildinfo.BuildInfo.name, buildinfo.BuildInfo.version),
      opt[String]('o', "output")
        .required()
        .action((o, c) => c.copy(output = o))
        .text("Output destination path"),
      opt[Int]('s', "scalaVersion")
        .optional()
        .action((v, c) => c.copy(scalaVersion = v))
        .validate(v => if (Seq(2, 3).contains(v)) {
          success
        } else {
          failure("scalaVersion must be either 2 or 3")
        })
        .text("Scala version to emit. Defaults to 2.x"),
      note(
        """This is meant to be used in conjunction with the Prisma CLI as a generator.
          |It listens on stdin as a jsonrpc 2.0 interface and emits on stderr.""".stripMargin)
    )
  }
}
