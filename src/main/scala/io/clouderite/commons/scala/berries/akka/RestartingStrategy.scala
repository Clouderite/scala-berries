package io.clouderite.commons.scala.berries.akka

import akka.actor.SupervisorStrategy.{escalate, restart, stop}
import akka.actor.{Actor, ActorInitializationException, ActorKilledException, ActorLogging, OneForOneStrategy}

import scala.concurrent.duration.DurationInt

trait RestartingStrategy extends Actor with ActorLogging {
  override val supervisorStrategy: OneForOneStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minutes) {
    case _: ActorInitializationException ⇒
      restart
      
    case _: ActorKilledException ⇒
      stop

    case ex: Error ⇒
      log.error(ex, "Error occurred - escalating")
      escalate

    case ex ⇒
      log.error(ex, "Exception occurred - restarting")
      restart
  }
}
