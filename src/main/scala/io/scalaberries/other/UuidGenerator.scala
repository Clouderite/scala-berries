package io.scalaberries.other

import java.util.UUID

trait UUIDMapper[T] {
  def map(uuid: UUID): T
}

object UuidGenerator {
  def generate[T](implicit mapper: UUIDMapper[T]): T = {
    mapper.map(UUID.randomUUID())
  }

  implicit def toStringMapper = new UUIDMapper[String] {
    override def map(uuid: UUID): String = uuid.toString
  }
}
