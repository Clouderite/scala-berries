package io.clouderite.commons.scala.berries.collection

object CollectionOperations {
  implicit def toSeq[B, A <% B](l: Seq[A]): Seq[B] = l map { x ⇒ x: B }
  implicit def toSet[B, A <% B](l: Set[A]): Set[B] = l map { x ⇒ x: B }
}
