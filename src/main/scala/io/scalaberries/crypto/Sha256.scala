package io.scalaberries.crypto

import java.nio.file.Path
import java.security.MessageDigest

import io.scalaberries.io.FileOperations

object Sha256 {
  private val digest = MessageDigest.getInstance("SHA-256")

  def hashFile(path: Path)(implicit fileOperations: FileOperations): Array[Byte] = {
    val data = fileOperations.readAllBytes(path)
    digest.update(data)
    digest.digest()
  }

  digest.digest().map("%02x" format _).mkString
}
