package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{ActorRefRoutee, Router, SmallestMailboxRoutingLogic}
import com.google.inject.name.{Named, Names}
import com.google.inject.{Guice, Injector, Key}
import com.linehrr.akka.http.injector.AppInjector
import javax.inject.Inject

class ParseActor extends Actor {

  val injector: Injector = Guice.createInjector(new AppInjector)
  val factory = injector.getInstance(Key.get(classOf[Factory], Names.named("worker")))
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

