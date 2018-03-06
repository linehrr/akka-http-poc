package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.google.inject.Inject
import com.google.inject.name.Named
import com.twg.logic.IParameterParser

class Worker(parserLogic: IParameterParser) extends Actor {

  override def preStart(): Unit = {
    // init some objs

    super.preStart()
  }

  override def postStop(): Unit = {
    // close all closable objs

    super.postStop()
    parserLogic.close()
  }

  override def receive: Receive = {
    case (name: String, age: String) => {
      sender() ! s"name: $name, age: $age"
    }
  }
}

@Named("worker")
class WorkerFactory extends IFactory {
  @Inject var system: ActorSystem = null

  override def get(): ActorRef = {
    @Inject var parserLogic: IParameterParser = null
    system.actorOf(Props(classOf[Worker], parserLogic))
  }
}