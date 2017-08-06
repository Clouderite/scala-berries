package io.clouderite.commons.scala.berries.collection

object CollectionOperations {
  implicit def toList[B, A <% B](l: List[A]): List[B] = l map { x ⇒ x: B }
  implicit def toSet[B, A <% B](l: Set[A]): Set[B] = l map { x ⇒ x: B }
}
