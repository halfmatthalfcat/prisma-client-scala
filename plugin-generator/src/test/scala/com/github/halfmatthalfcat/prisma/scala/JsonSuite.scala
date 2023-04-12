package com.github.halfmatthalfcat.prisma.scala

import com.github.halfmatthalfcat.prisma.scala.Json.{FromJson, ToJson}
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromString, writeToString}
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

class JsonSuite extends munit.FunSuite {
  test("deserializes a string") {
    val result = readFromString[Json]("\"hello world\"")
    assert (result match {
      case JsonString(str) => str == "hello world"
      case _ => false
    })
  }

  test("deserializes an int") {
    val result = readFromString[Json]("12345")
    assert (result match {
      case JsonNumber(number) => number == 12345
      case _ => false
    })
  }

  test("deserializes a bool") {
    val tResult = readFromString[Json]("true")
    val fResult = readFromString[Json]("false")

    assert (tResult match {
      case JsonBool(bool) => bool
      case _ => false
    })

    assert(fResult match {
      case JsonBool(bool) => !bool
      case _ => false
    })
  }

  test("deserializes null") {
    val result = readFromString[Json]("null")

    assert (result match {
      case JsonNull => true
      case _ => false
    })
  }

  test("deserializes an arbitrary object") {
    val result = readFromString[Json]("{\"a\":123,\"b\":false}")

    assert (result match {
      case JsonObject(elements) => (for {
        a <- elements.get("a").map {
          case JsonNumber(number) => number == 123
          case _ => false
        }
        b <- elements.get("b").map {
          case JsonBool(bool) => !bool
          case _ => false
        }
      } yield a && b).getOrElse(false)
      case _ => false
    })
  }

  test("deserializes an arbitrary array") {
    val result = readFromString[Json]("[1, false, \"hello world\"]")

    assert (result match {
      case JsonArray(elements) => (for {
        first <- elements.headOption.map {
          case JsonNumber(number) => number == 1
          case _ => false
        }
        second <- elements.lift(1).map {
          case JsonBool(bool) => !bool
          case _ => false
        }
        third <- elements.lift(2).map {
          case JsonString(str) => str == "hello world"
          case _ => false
        }
      } yield first && second && third).getOrElse(false)
      case _ => false
    })
  }

  test("deserializes a complex object") {
    val result = readFromString[Json](
      s"""{
         |  "a": 123,
         |  "b": true,
         |  "c": {
         |    "a": {
         |      "a": [1, 2, 3],
         |      "b": false
         |    }
         |  }
         |}""".stripMargin)

    assert (result match {
      case JsonObject(elements) => (for {
        a <- elements.get("a").map {
          case JsonNumber(number) => number == 123
          case _ => false
        }
        b <- elements.get("b").map {
          case JsonBool(bool) => bool
          case _ => false
        }
        c <- elements.get("c").map {
          case JsonObject(elements2) => elements2.get("a").exists {
            case JsonObject(elements3) => (for {
              nestedA <- elements3.get("a").map {
                case JsonArray(elements) => (for {
                  first <- elements.headOption.map {
                    case JsonNumber(number) => number == 1
                    case _ => false
                  }
                  second <- elements.lift(1).map {
                    case JsonNumber(number) => number == 2
                    case _ => false
                  }
                  third <- elements.lift(2).map {
                    case JsonNumber(number) => number == 3
                    case _ => false
                  }
                } yield first && second && third).getOrElse(false)
                case _ => false
              }
              nestedB <- elements3.get("b").map {
                case JsonBool(bool) => !bool
                case _ => false
              }
            } yield nestedA && nestedB).getOrElse(false)
            case _ => false
          }
          case _ => false
        }
      } yield a && b && c).getOrElse(false)
      case _ => false
    })
  }

  test("deserializes a complex array") {
    val result = readFromString[Json](
      s"""[
         | { "a": 1, "b": 2, "c": false },
         | 123,
         | { "a": { "a": 1 } }
         |]""".stripMargin)

    assert (result match {
      case JsonArray(elements) => (for {
        case first <- elements.headOption.map {
          case JsonObject(elements) => (for {
            a <- elements.get("a").map {
              case JsonNumber(number) => number == 1
              case _ => false
            }
            b <- elements.get("b").map {
              case JsonNumber(number) => number == 2
              case _ => false
            }
            c <- elements.get("c").map {
              case JsonBool(bool) => !bool
              case _ => false
            }
          } yield a && b && c).getOrElse(false)
          case _ => false
        }
        case second <- elements.lift(1).map {
          case JsonNumber(number) => number == 123
          case _ => false
        }
        case third <- elements.lift(2).map {
          case JsonObject(elements) => elements.get("a").exists {
            case JsonObject(elements2) => elements2.get("a").exists {
              case JsonNumber(number) => number == 1
              case _ => false
            }
            case _ => false
          }
          case _ => false
        }
      } yield first && second && third).getOrElse(false)
      case _ => false
    })
  }

  test("convert string object to case class") {
    case class A(a: String, b: Boolean, c: Int)
    object A {
      val codec: JsonValueCodec[A] = JsonCodecMaker.make
      implicit val fromJson: FromJson[A] = readFromString[A](_)(codec)
    }

    val result = readFromString[Json](
      s"""{
         |  "a": "hello world",
         |  "b": false,
         |  "c": 123
         |}""".stripMargin)

    assert (result match {
      case _: JsonObject => true
      case _ => false
    })

    val convertedResult = result
      .asInstanceOf[JsonObject]
      .to[A]

    assertEquals(
      A("hello world", false, 123),
      convertedResult,
    )
  }

  test("convert a case class into a JSON type") {
    import Json._

    case class A(a: String, b: Boolean, c: Int)
    object A {
      val codec: JsonValueCodec[A] = JsonCodecMaker.make
      implicit val toJson: ToJson[A] = writeToString(_)(codec)
    }

    val json = JsonObject.from(A(
      "hello world",
      false,
      123,
    ))

    assert (json match {
      case JsonObject(elements) => (for {
        a <- elements.get("a").map {
          case JsonString(str) => str == "hello world"
          case _ => false
        }
        b <- elements.get("b").map {
          case JsonBool(bool) => !bool
          case _ => false
        }
        c <- elements.get("c").map {
          case JsonNumber(number) => number == 123
          case _ => false
        }
      } yield a && b && c).getOrElse(false)
      case _ => false
    })
  }

  test("convert an array string to a seq of case classes") {
    case class A(a: String, b: Boolean, c: Int)
    object A {
      val codec: JsonValueCodec[A] = JsonCodecMaker.make
      implicit val fromJson: FromJson[A] = readFromString[A](_)(codec)
    }

    val result = readFromString[Json](
      s"""[{
         |  "a": "hello world",
         |  "b": false,
         |  "c": 123
         |}, {
         |  "a": "hello world again",
         |  "b": true,
         |  "c": 456
         |}]""".stripMargin)

    assert(result match {
      case _: JsonArray => true
      case _ => false
    })

    val convertedResult = result
      .asInstanceOf[JsonArray]
      .to[A]

    assertEquals(
      Seq(
        A("hello world", false, 123),
        A("hello world again", true, 456),
      ),
      convertedResult,
    )
  }

  test("convert a seq of case classes into a JSON type") {
    case class A(a: String, b: Boolean, c: Int)
    object A {
      val codec: JsonValueCodec[A] = JsonCodecMaker.make
      implicit val toJson: ToJson[A] = writeToString(_)(codec)
    }

    val json = JsonArray.from(Seq(
      A("hello world", false, 123),
      A("hello world again", true, 456),
    ))

    assert (json match {
      case JsonArray(elements) => (for {
        first <- elements.headOption.map {
          case JsonObject(elements) => (for {
            a <- elements.get("a").map {
              case JsonString(str) => str == "hello world"
              case _ => false
            }
            b <- elements.get("b").map {
              case JsonBool(bool) => !bool
              case _ => false
            }
            c <- elements.get("c").map {
              case JsonNumber(number) => number == 123
              case _ => false
            }
          } yield a && b && c).getOrElse(false)
          case _ => false
        }
        second <- elements.lift(1).map {
          case JsonObject(elements) => (for {
            a <- elements.get("a").map {
              case JsonString(str) => str == "hello world again"
              case _ => false
            }
            b <- elements.get("b").map {
              case JsonBool(bool) => bool
              case _ => false
            }
            c <- elements.get("c").map {
              case JsonNumber(number) => number == 456
              case _ => false
            }
          } yield a && b && c).getOrElse(false)
          case _ => false
        }
      } yield first && second).getOrElse(false)
      case _ => false
    })
  }
}
