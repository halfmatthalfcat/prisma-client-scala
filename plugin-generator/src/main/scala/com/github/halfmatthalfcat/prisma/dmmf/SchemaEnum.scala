package com.github.halfmatthalfcat.prisma.dmmf

import com.github.halfmatthalfcat.prisma.scala.{Scala2, Scala3, ScalaGenVersion}
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.meta._

case class SchemaEnum(
  name: String,
  values: Seq[String]
) {
  def code()(implicit version: ScalaGenVersion) = version match {
    case Scala2 =>
      val nameTerm = Term.Name(name)
      val nameType = Type.Name(name)
      val pats = values.map(v => Pat.Var(Term.Name(v)))

      q"""
         object $nameTerm extends Enumeration {
           type $nameType = Value

           val ..${pats.toList} = Value
         }
       """
    case Scala3 =>
      import scala.meta.dialects.Scala3
      val cases = Defn.RepeatedEnumCase(
        List.empty,
        values.map(v => Term.Name(v)).toList,
      )

      q"""
         enum ${Type.Name(name)} {
           $cases
         }
       """
  }

  def codec()(implicit version: ScalaGenVersion): Tree = version match {
    case Scala2 =>
      val nameTerm = Term.Name(name)
      val nameType = Type.Name(name)

      val c = q"""
       implicit val codec: JsonValueCodec[$nameType] = new JsonValueCodec[$nameType] {
         override def decodeValue(in: JsonReader, default: $nameType): $nameType = {
           val b = in.nextToken()
           if (b == '"') {
             in.rollbackToken()
             val str = in.readString(null)
             $nameTerm.values.find(_.toString == str).getOrElse(
               in.enumValueError("expected " + $name)
             )
           } else in.decodeError("expected " + $name)
         }

         override def encodeValue(x: $nameType, out: JsonWriter): Unit =
           out.writeVal(x.toString)

         override def nullValue: $nameType = null.asInstanceOf[$nameType]
       }
     """
     source"""
       import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
       import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

       $c
     """
    case Scala3 =>
      import scala.meta.dialects.Scala3
      val nameTerm = Term.Name(name)
      val nameType = Type.Name(name)

      val c = q"""
        given JsonValueCodec[$nameType] = new JsonValueCodec[$nameType] {
          override def decodeValue(in: JsonReader, default: $nameType): $nameType = {
            val b = in.nextToken()
            if (b == '"') {
              in.rollbackToken()
              val str = in.readString(null)
              $nameTerm.values.find(_.toString == str).getOrElse(
                in.enumValueError("expected " + $name)
              )
            } else in.decodeError("expected " + $name)
          }

          override def encodeValue(x: $nameType, out: JsonWriter): Unit =
            out.writeVal(x.toString)

          override def nullValue: $nameType = null.asInstanceOf[$nameType]
       }
      """
      source"""
        import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
        import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

        $c
      """
  }
}
object SchemaEnum {
  implicit val codec: JsonValueCodec[SchemaEnum] = JsonCodecMaker.make
}
