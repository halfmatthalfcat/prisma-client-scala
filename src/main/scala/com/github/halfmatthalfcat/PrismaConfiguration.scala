package com.github.halfmatthalfcat

case class PrismaConfiguration(
  version: String = "4.7.1",
  engineVersion: String = "272861e07ab64f234d3ffc4094e32bd61775599c",
  prismaUrl: String = "https://packaged-cli.prisma.sh",
  engineUrl: String = "https://binaries.prisma.sh/all_commits",
  outDir: String,
)
