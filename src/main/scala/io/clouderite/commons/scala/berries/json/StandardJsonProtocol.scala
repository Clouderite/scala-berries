package io.clouderite.commons.scala.berries.json

import java.nio.file.{Path, Paths}
import java.time.Instant

import spray.json.{DeserializationException, JsFalse, JsNumber, JsString, JsTrue, JsValue, RootJsonFormat}

object StandardJsonProtocol {
  implicit val instantJson = InstantJsonFormat
  implicit val pathJson = PathJsonFormat
  implicit val anyJson = PathJsonFormat
}

object InstantJsonFormat extends RootJsonFormat[Instant] {
  def write(obj: Instant): JsValue = JsNumber(obj.toEpochMilli)

  def read(json: JsValue): Instant = json match {
    case JsNumber(number) => Instant.ofEpochMilli(number.toLong)
    case _ => throw DeserializationException("JsNumber expected")
  }
}

object PathJsonFormat extends RootJsonFormat[Path] {
  def write(obj: Path): JsValue = JsString(obj.toString)

  def read(json: JsValue): Path = json match {
    case JsString(str) => Paths.get(str)
    case _ => throw DeserializationException("JsString expected")
  }
}

object AnyJsonFormat extends RootJsonFormat[Any] {
  def write(x: Any): JsValue = x match {
    case n: Int => JsNumber(n)
    case n: Long => JsNumber(n)
    case n: Double => JsNumber(n)
    case n: BigDecimal => JsNumber(n)
    case s: String => JsString(s)
    case b: Boolean if b => JsTrue
    case b: Boolean if !b => JsFalse
  }
  
  def read(value: JsValue): Any = value match {
    case JsNumber(n) => n.doubleValue
    case JsString(s) => s
    case JsTrue => true
    case JsFalse => false
  }
}