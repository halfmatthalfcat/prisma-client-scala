package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

object FieldLocation extends Enumeration {
  type FieldLocation = Value
  
  protected case class FieldLocationVal(location: String) extends super.Val
  implicit def valueToFieldLocationVal(x: Value): FieldLocationVal = x.asInstanceOf[FieldLocationVal]
  implicit val codec: JsonValueCodec[FieldLocation] = new JsonValueCodec[FieldLocation] {
    override def decodeValue(in: JsonReader, default: FieldLocation): FieldLocation = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        FieldLocation.values.find(_.location == str).getOrElse(
          in.enumValueError("expected FieldLocation")
        )
      } else in.decodeError("expected FieldLocation")
    }

    override def encodeValue(x: FieldLocation, out: JsonWriter): Unit =
      out.writeVal(x.location)

    override def nullValue: FieldLocation = null.asInstanceOf[FieldLocation]
  }
  
  val Scalar: FieldLocationVal = FieldLocationVal("scalar")
  val InputObjectTypes: FieldLocationVal = FieldLocationVal("inputObjectTypes")
  val OutputObjectTypes: FieldLocationVal = FieldLocationVal("outputObjectTypes")
  val EnumTypes: FieldLocationVal = FieldLocationVal("enumTypes")
  val FieldRefTypes: FieldLocationVal = FieldLocationVal("fieldRefTypes")
}
