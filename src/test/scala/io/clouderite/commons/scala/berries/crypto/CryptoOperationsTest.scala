package io.clouderite.commons.scala.berries.crypto

import java.security.{InvalidAlgorithmParameterException, InvalidKeyException}

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.{Failure, Try}

class CryptoOperationsTest extends FlatSpec with GeneratorDrivenPropertyChecks with MustMatchers {
  "decrypt" should "return original plaintext before encryption (128 bit)" in {
    encryptDecryptFixture(16, 16)
  }

  "decrypt" should "return original plaintext before encryption (256 bit)" in {
    encryptDecryptFixture(32, 16)
  }

  "encrypt" should "throw exception when key has invalid size" in {
    forAll(Gen.choose(1, 1000)) { invalidKeySize ⇒
      whenever(invalidKeySize != 16 && invalidKeySize != 32) {
        encryptDecryptFixture(invalidKeySize, 16) {
          case Failure(ex) ⇒ ex mustBe a[InvalidKeyException]
        }
      }
    }
  }

  "encrypt" should "throw exception when key is empty" in {
    encryptDecryptFixture(0, 16) {
      case Failure(ex) ⇒ ex mustBe a[IllegalArgumentException]
    }
  }

  "encrypt" should "throw exception when iv is empty" in {
    encryptDecryptFixture(16, 0) {
      case Failure(ex) ⇒ ex mustBe a[InvalidAlgorithmParameterException]
    }
  }

  "encrypt" should "throw exception when iv has invalid size" in {
    forAll(Gen.choose(1, 1000)) { invalidIvSIze ⇒
      whenever(invalidIvSIze != 16 && invalidIvSIze != 32) {
        encryptDecryptFixture(16, invalidIvSIze) {
          case Failure(ex) ⇒ ex mustBe a[InvalidAlgorithmParameterException]
        }
      }
    }
  }

  private def encryptDecryptFixture(keySize: Int, ivSize: Int)(implicit finalCheck: (Try[_]) ⇒ Unit = emptyCheck) = {
    forAll(Gen.alphaStr) { plainText ⇒
      forAll(generateString(keySize)) { key ⇒
        forAll(generateString(ivSize)) { iv ⇒
          finalCheck {
            Try {
              val sut = new CryptoOperations(plainText)
              val encrypted = sut.encrypt(key, iv)

              val sut2 = new CryptoOperations(encrypted)
              val decrypted = sut2.decrypt(key, iv)

              plainText mustBe decrypted
              encrypted must not be plainText
            }
          }
        }
      }
    }
  }

  private def emptyCheck(result: Try[_]): Unit = {}
  private def generateString(len: Int) = Gen.listOfN(len, Gen.alphaChar).map(_.mkString)
}
