package io.clouderite.commons.scala.berries.akka

import akka.actor.Actor

trait ActorEmptyReceive extends Actor {
  override def receive: Receive = {
    case _ =>
  }
}
