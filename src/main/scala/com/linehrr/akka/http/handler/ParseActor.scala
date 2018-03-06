package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing.{ActorRefRoutee, Router, SmallestMailboxRoutingLogic}
import com.google.inject.name.Named
import javax.inject.Inject

class ParseActor(factory: IFactory) extends Actor {
  val router: Router = {
    val routees = Vector.fill(20) {

      val r = factory.get()
      context watch r
      ActorRefRoutee(r)
    }

    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case (name: String, age: String) => router.route((name, age), sender())
    case Terminated(actor) => {
      router.removeRoutee(actor)
      val r = factory.get()
      context watch r
      router.addRoutee(r)
    }
  }
}

@Named("parser")
class ParserFactory extends IFactory {
  @Inject var system: ActorSystem = null

  @Inject @Named("worker") var workerFactory: IFactory = null

  override def get(): ActorRef = {
    system.actorOf(Props(classOf[ParseActor], workerFactory))
  }
}