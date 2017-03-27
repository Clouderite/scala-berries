package io.scalaberries.crypto

class HashOperations(data: Array[Byte]) {
  def toHex: String = {
    data.map("%02x" format _).mkString
  }
}

object HashOperations {
  implicit def toHashOperations(data: Array[Byte]): HashOperations = {
    new HashOperations(data)
  }
}
