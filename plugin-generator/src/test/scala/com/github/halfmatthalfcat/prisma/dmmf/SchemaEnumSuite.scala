package com.github.halfmatthalfcat.prisma.dmmf

import com.github.halfmatthalfcat.prisma.scala.{Scala2, Scala3, ScalaGenVersion}

class SchemaEnumSuite extends munit.FunSuite {
  test("produce a valid Scala 2 enum") {
    implicit val version: ScalaGenVersion = Scala2
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.code.syntax,
      """object Test extends Enumeration {
        |  type Test = Value
        |  val A, B, C = Value
        |}""".stripMargin
    )
  }

  test("produce a valid Scala 2 enum codec") {
    implicit val version: ScalaGenVersion = Scala2
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.codec.syntax,
      """import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
        |import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
        |implicit val codec: JsonValueCodec[Test] = new JsonValueCodec[Test] {
        |  override def decodeValue(in: JsonReader, default: Test): Test = {
        |    val b = in.nextToken()
        |    if (b == '"') {
        |      in.rollbackToken()
        |      val str = in.readString(null)
        |      Test.values.find(_.toString == str).getOrElse(in.enumValueError("expected " + "Test"))
        |    } else in.decodeError("expected " + "Test")
        |  }
        |  override def encodeValue(x: Test, out: JsonWriter): Unit = out.writeVal(x.toString)
        |  override def nullValue: Test = null.asInstanceOf[Test]
        |}""".stripMargin
    )
  }

  test("produce a valid Scala 3 enum") {
    implicit val version: ScalaGenVersion = Scala3
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.code.syntax,
      """enum Test { case A, B, C }"""
    )
  }

  test("produce a valid Scala 3 enum codec") {
    implicit val version: ScalaGenVersion = Scala3
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.codec.syntax,
      """import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
        |import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
        |given JsonValueCodec[Test] = new JsonValueCodec[Test] {
        |  override def decodeValue(in: JsonReader, default: Test): Test = {
        |    val b = in.nextToken()
        |    if (b == '"') {
        |      in.rollbackToken()
        |      val str = in.readString(null)
        |      Test.values.find(_.toString == str).getOrElse(in.enumValueError("expected " + "Test"))
        |    } else in.decodeError("expected " + "Test")
        |  }
        |  override def encodeValue(x: Test, out: JsonWriter): Unit = out.writeVal(x.toString)
        |  override def nullValue: Test = null.asInstanceOf[Test]
        |}""".stripMargin
    )
  }
}
