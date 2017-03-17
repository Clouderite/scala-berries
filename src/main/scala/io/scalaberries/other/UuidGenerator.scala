package io.scalaberries.other

import java.util.UUID

object UuidGenerator {

  def generate(): UUID = {
    UUID.randomUUID()
  }
}
