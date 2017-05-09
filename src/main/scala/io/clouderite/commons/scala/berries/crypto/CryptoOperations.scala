package io.clouderite.commons.scala.berries.crypto

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}

class CryptoOperations(text: String) {
  private val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

  def encrypt(key: String, iv: String): String = {
    cipher.init(Cipher.ENCRYPT_MODE, keySpec(key), paramSpec(iv))
    Base64.getEncoder.encodeToString(cipher.doFinal(text.getBytes()))
  }

  def decrypt(key: String, iv: String): String = {
    cipher.init(Cipher.DECRYPT_MODE, keySpec(key), paramSpec(iv))
    new String(cipher.doFinal(Base64.getDecoder.decode(text)))
  }

  def keySpec(key: String) = new SecretKeySpec(key.getBytes(), "AES")
  def paramSpec(iv: String) = new IvParameterSpec(iv.getBytes())
}

object CryptoOperations {
  implicit def toCryptoOperations(text: String): CryptoOperations = new CryptoOperations(text)
}