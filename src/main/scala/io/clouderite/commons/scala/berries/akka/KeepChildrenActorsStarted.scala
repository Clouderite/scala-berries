package io.clouderite.commons.scala.berries.akka

import akka.actor.Actor

trait KeepChildrenActorsStarted extends Actor {
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    postStop()
  }
}
