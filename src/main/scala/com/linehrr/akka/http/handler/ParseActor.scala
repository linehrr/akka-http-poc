package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.{ActorRefRoutee, Router, SmallestMailboxRoutingLogic}
import com.google.inject.name.{Named, Names}
import com.google.inject.{Guice, Injector, Key}
import com.linehrr.akka.http.injector.AppInjector
import javax.inject.Inject

class ParseActor(factory: Factory) extends Actor {
  val router: Router = {
    val routees = Vector.fill(5) {

      val r = factory.get()
      context watch r
      ActorRefRoutee(r)
    }

    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case (name: String, age: String) => router.route((name, age), sender())
  }
}

@Named("parser")
class ParserFactory extends Factory {
  @Inject var system: ActorSystem = null

  @Inject @Named("worker") var workerFactory: Factory = null

  override def get(): ActorRef = {
    system.actorOf(Props(classOf[ParseActor], workerFactory))
  }
}