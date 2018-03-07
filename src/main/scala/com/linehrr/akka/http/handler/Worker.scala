package com.linehrr.akka.http.handler

import akka.actor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.google.inject.{Inject, Key}
import com.google.inject.name.{Named, Names}
import com.linehrr.akka.http.injector.AppInjector
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo
import com.twg.logic.IParameterParser

@Named("worker-actor")
class Worker extends Actor {

  @Inject var parserLogic:IParameterParser = null

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
    system.actorOf(Props(classOf[Worker]))
  }

  override def getActor(): Actor = {
    AppInjector().getInstance(Key.get(classOf[Actor], Names.named("worker-actor")))
  }
}