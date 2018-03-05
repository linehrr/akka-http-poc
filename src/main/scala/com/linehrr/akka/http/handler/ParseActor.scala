package com.linehrr.akka.http.handler

import akka.actor.{Actor, Props}
import akka.routing.{ActorRefRoutee, Router, SmallestMailboxRoutingLogic}
import com.google.inject.{Guice, Injector}
import com.linehrr.akka.http.injector.AppInjector

class ParseActor extends Actor {

  val injector: Injector = Guice.createInjector(new AppInjector)

  val router: Router = {
    val routees = Vector.fill(5) {

      val r = context.actorOf(Props(classOf[Worker], injector.getInstance(classOf[IParser])))
      context watch r
      ActorRefRoutee(r)
    }

    Router(SmallestMailboxRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case (name: String, age: String) => router.route((name, age), sender())
  }
}

class Worker(parser: IParser) extends Actor {
  override def receive: Receive = {
    case (name: String, age: String) => {
      sender() ! parser.parse(name, age)
    }
  }
}