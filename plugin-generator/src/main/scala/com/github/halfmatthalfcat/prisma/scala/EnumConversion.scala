package com.github.halfmatthalfcat.prisma.scala

import com.github.halfmatthalfcat.prisma.dmmf.SchemaEnum

import scala.meta._

object EnumConversion {
  def code(`enum`: SchemaEnum)(implicit version: ScalaGenVersion) = version match {
    case Scala2 =>
      val nameTerm = Term.Name(`enum`.name)
      val nameType = Type.Name(`enum`.name)
      val pats = `enum`.values.map(v => Pat.Var(Term.Name(v)))

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
        `enum`.values.map(v => Term.Name(v)).toList,
      )

      q"""
         enum ${Type.Name(`enum`.name)} {
           $cases
         }
       """
  }

  def codec(`enum`: SchemaEnum)(implicit version: ScalaGenVersion): Tree = version match {
    case Scala2 =>
      val name = `enum`.name
      val nameTerm = Term.Name(`enum`.name)
      val nameType = Type.Name(`enum`.name)

      val c =
        q"""
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
      val name = `enum`.name
      val nameTerm = Term.Name(`enum`.name)
      val nameType = Type.Name(`enum`.name)

      val c =
        q"""
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
