package com.github.halfmatthalfcat.prisma.scala

import scala.meta.{Import => MetaImport, _}

object ScalaImport {
  sealed trait Import {
    val `import`: MetaImport
  }

  case object JsoniterCore extends Import {
    val `import`: MetaImport = q"import com.github.plokhotnyuk.jsoniter_scala.core._"
  }

  case object JsoniterMacro extends Import {
    val `import`: MetaImport = q"import com.github.plokhotnyuk.jsoniter_scala.macros._"
  }
}
