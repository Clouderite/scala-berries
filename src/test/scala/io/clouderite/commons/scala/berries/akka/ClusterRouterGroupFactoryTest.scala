package io.clouderite.commons.scala.berries.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.pattern.ask
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import akka.testkit.{TestKit, TestProbe}
import akka.util.Timeout
import org.scalacheck.Gen
import org.scalacheck.Gen.{alphaChar, listOf}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

import scala.concurrent.Await
import scala.concurrent.duration.{DurationDouble, FiniteDuration}

class ClusterRouterGroupFactoryTest extends TestKit(ActorSystem("test")) with FlatSpecLike with BeforeAndAfterAll with MustMatchers with GeneratorDrivenPropertyChecks {
  private val duration: FiniteDuration = 5 seconds
  private implicit val timeout: Timeout = Timeout.durationToTimeout(duration)
  private val TestRouteePath = "routee_path"
  private val TestRole = "test_role"

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withParent(f: (ActorWithName) ⇒ Any) = {
    withNonEmptyName { testName ⇒
      Cluster(system)
      val parent = system.actorOf(Props[TestActor], testName)
      f(ActorWithName(parent, testName))
    }
  }

  private def withSpecification(f: (ClusterRouterGroupSpecification) ⇒ Any) = {
    withNonEmptyName { testName ⇒
      val specification = ClusterRouterGroupSpecification(testName, TestRouteePath, TestRole)
      f(specification)
    }
  }

  private def withNonEmptyName(f: (String) ⇒ Any) = {
    forAll(Gen.nonEmptyListOf(alphaChar).map(_.mkString)) { testName ⇒
      f(testName)
    }
  }

  "consistentHashingGroup" must "create cluster router actor with given name" in {
    withParent { parent ⇒
      withSpecification { specification ⇒
        def hashMapping: ConsistentHashMapping = {
          case _ ⇒
        }

        val future = parent.ref.ask(("consistentHashing", specification, hashMapping))(timeout)

        val result = Await.result(future, duration).asInstanceOf[ActorRef]
        assertHasPath(result, "/user/" + parent.name + "/" + specification.name)
      }
    }
  }

  private def assertHasPath(actorRef: ActorRef, path: String) {
    implicit val executionContext = system.dispatcher
    val selectedActor = Await.result(system.actorSelection(path).resolveOne(), duration)
    actorRef mustBe selectedActor
  }
}

class TestActor extends Actor {
  override def receive: Receive = {
    case ("consistentHashing", specification: ClusterRouterGroupSpecification, hashMapping: ConsistentHashMapping) ⇒
      sender ! ClusterRouterGroupFactory.consistentHashingGroup(specification)(hashMapping)

    case ("random", specification: ClusterRouterGroupSpecification) ⇒
      sender ! ClusterRouterGroupFactory.randomGroup(specification)
  }
}

case class ActorWithName(ref: ActorRef, name: String)