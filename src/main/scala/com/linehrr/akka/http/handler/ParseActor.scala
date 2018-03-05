package com.linehrr.akka.http.handler

import akka.actor.{Actor, Props}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

class ParseActor extends Actor {

  val router: Router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props(new Worker))
      context watch r
      ActorRefRoutee(r)
    }

//    Router(SmallestMailboxRoutingLogic(), routees)
    Router(RoundRobinRoutingLogic() ,routees)
  }

  override def receive: Receive = {
    case (name, age) => router.route((name, age), sender())
  }
}

class Worker extends Actor {
  override def receive: Receive = {
    case (name, age) => sender() ! s"name: $name, age: $age, worker: $self"
  }
}