package io.clouderite.commons.scala.berries.crypto
import io.clouderite.commons.scala.berries.crypto.HashOperations.toHashOperations

class HashOperations(data: Array[Byte]) {
  def toHex: String = {
    data.map("%02x" format _).mkString
  }

  def hash: String = {
    Sha256.hash(data).toHex
  }
}

object HashOperations {
  implicit def toHashOperations(data: Array[Byte]): HashOperations = {
    new HashOperations(data)
  }

  implicit def toHashOperations(data: String): HashOperations = {
    new HashOperations(data.getBytes())
  }
}
