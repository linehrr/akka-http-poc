package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.google.inject.Inject
import com.google.inject.name.Named

class Worker extends Actor {
  override def receive: Receive = {
    case (name: String, age: String) => {
      sender() ! s"name: $name, age: $age"
    }
  }
}

@Named("worker")
class WorkerFactory extends Factory {
  @Inject var system: ActorSystem = null

  override def get(): ActorRef = {

    system.actorOf(Props[Worker])
  }
}