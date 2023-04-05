package com.github.halfmatthalfcat.prisma.scala

import com.github.plokhotnyuk.jsoniter_scala.core.readFromString

class JsonSuite extends munit.FunSuite {
  test("correctly deserializes a string") {
    val result = readFromString[Json]("\"hello world\"")
    assert (result match {
      case JsonString(str) => str == "hello world"
      case _ => false
    })
  }

  test("correctly deserializes an int") {
    val result = readFromString[Json]("12345")
    assert (result match {
      case JsonNumber(number) => number == 12345
      case _ => false
    })
  }

  test("correctly deserializes a bool") {
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

  test("correctly deserializes null") {
    val result = readFromString[Json]("null")

    assert (result match {
      case JsonNull => true
      case _ => false
    })
  }

  test("correctly deserializes an arbitrary object") {
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

  test("correctly deserializes an arbitrary array") {
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

  test("correctly deserializes a complex object") {
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

  test("correctly deserializes a complex array") {
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
}
