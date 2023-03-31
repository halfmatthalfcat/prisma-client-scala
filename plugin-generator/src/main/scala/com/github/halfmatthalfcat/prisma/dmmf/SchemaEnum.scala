package com.github.halfmatthalfcat.prisma.dmmf

import com.github.halfmatthalfcat.prisma.scala.{ScalaGeneratable, ScalaImport}
import com.github.halfmatthalfcat.prisma.scala.ScalaImport.{JsoniterCore, JsoniterMacro}
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.meta._

case class SchemaEnum(
  name: String,
  values: Seq[String]
) extends ScalaGeneratable {
  override val genImport: Seq[ScalaImport.Import] = Seq(
    JsoniterCore,
    JsoniterMacro,
  )

  override def scala2(): Defn = {
    val pats = values.map(v => Pat.Var(Term.Name(v)))
    q"""
       object ${Term.Name(name)} extends Enumeration {
         type ${Type.Name(name)} = Value

         val ..${pats.toList} = Value
       }
     """
  }

  override def scala2Codec(): Option[Defn] = {
    val typeName = Type.Name(name)
    Some(
      q"""
       implicit val codec: JsonValueCodec[$typeName] = new JsonValueCodec[$typeName] {
         override def decodeValue(in: JsonReader, default: $typeName): $typeName = {
           val b = in.nextToken()
           if (b == '"') {
             in.rollbackToken()
             val str = in.readString(null)
             ${Term.Name(name)}.values.find(_.toString == str).getOrElse(
               in.enumValueError("expected " + $name)
             )
           } else in.decodeError("expected " + $name)
         }

         override def encodeValue(x: $typeName, out: JsonWriter): Unit =
           out.writeVal(x.toString)

         override def nullValue: $typeName = null.asInstanceOf[$typeName]
       }
     """)
  }

  override def scala3(): Defn = {
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

  override def scala3Codec(): Option[Defn] = {
    import scala.meta.dialects.Scala3
    val typeName = Type.Name(name)

    Some(
      q"""
        given JsonValueCodec[$typeName] = new JsonValueCodec[$typeName] {
          override def decodeValue(in: JsonReader, default: $typeName): $typeName = {
            val b = in.nextToken()
            if (b == '"') {
              in.rollbackToken()
              val str = in.readString(null)
              ${Term.Name(name)}.values.find(_.toString == str).getOrElse(
                in.enumValueError("expected " + $name)
              )
            } else in.decodeError("expected " + $name)
          }

          override def encodeValue(x: $typeName, out: JsonWriter): Unit =
            out.writeVal(x.toString)

          override def nullValue: $typeName = null.asInstanceOf[$typeName]
       }
       """)
  }
}
object SchemaEnum {
  implicit val codec: JsonValueCodec[SchemaEnum] = JsonCodecMaker.make
}
