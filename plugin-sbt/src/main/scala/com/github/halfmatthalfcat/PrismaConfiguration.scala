package com.github.halfmatthalfcat

case class PrismaConfiguration(
  cliVersion: String = "4.7.1",
  engineVersion: String = "272861e07ab64f234d3ffc4094e32bd61775599c",
  cliUrl: String = "https://packaged-cli.prisma.sh",
  engineUrl: String = "https://binaries.prisma.sh/all_commits",
  outDir: String,
  schemaFile: String,
  resourcesDir: String,
  scalaVersion: ScalaVersion,
)
