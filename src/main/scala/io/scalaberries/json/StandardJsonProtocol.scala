package io.scalaberries.json

import java.nio.file.{Path, Paths}
import java.time.Instant

import spray.json.{DeserializationException, JsNumber, JsString, JsValue, RootJsonFormat}

object StandardJsonProtocol {
  implicit val instantJson = InstantJsonFormat
  implicit val pathJson = PathJsonFormat
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
