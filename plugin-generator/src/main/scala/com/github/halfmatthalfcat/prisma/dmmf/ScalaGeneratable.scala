//package com.github.halfmatthalfcat.prisma.dmmf
//
//import scala.meta.{Defn, Importee, Importer, Import as MetaImport, Term}
//
//trait ScalaGeneratable {
//  protected sealed trait Import {
//    val `import`: MetaImport
//  }
//  protected case object JsoniterCore extends Import {
//    val `import`: MetaImport = MetaImport(
//      importers = List(
//        Importer(
//          importees = List(Importee.Wildcard()),
//          ref = Term.Select(
//            name = Term.Name("core"),
//            qual = Term.Select(
//              name = Term.Name("jsoniter_scala"),
//              qual = Term.Select(
//                name = Term.Name("plokhotnyuk"),
//                qual = Term.Select(
//                  name = Term.Name("github"),
//                  qual = Term.Name("com"),
//                )
//              )
//            )
//          )
//        )
//      )
//    )
//  }
//  protected case object JsoniterMacro extends Import {
//    val `import`: MetaImport = MetaImport(
//      importers = List(
//        Importer(
//          importees = List(Importee.Wildcard()),
//          ref = Term.Select(
//            name = Term.Name("macros"),
//            qual = Term.Select(
//              name = Term.Name("jsoniter_scala"),
//              qual = Term.Select(
//                name = Term.Name("plokhotnyuk"),
//                qual = Term.Select(
//                  name = Term.Name("github"),
//                  qual = Term.Name("com"),
//                )
//              )
//            )
//          )
//        )
//      )
//    )
//  }
//
//  val genImport: Seq[Import] = Seq.empty
//
//  // The actual Scala2 source
//  def scala2(): Defn
//  // The Scala2 jsoniter (de)serialization codec
//  def scala2Codec(): Option[Defn]
//
//  // The actual Scala3 source
//  def scala3(): Defn
//  // The Scala3 jsoniter (de)serialization codec
//  def scala3Codec(): Option[Defn]
//}
