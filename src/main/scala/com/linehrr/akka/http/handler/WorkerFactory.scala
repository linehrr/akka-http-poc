package com.linehrr.akka.http.handler

import akka.actor.{Actor, Props}
import com.google.inject.name.Named
import com.linehrr.akka.http.injector.AppInjector
import com.twg.logic.IParameterParser

@Named("parser-worker")
class WorkerFactory extends ActorFactory {

  class Worker extends Actor {

    val parserLogic: IParameterParser = AppInjector.getParameterParser

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
        sender() ! parserLogic.parse(name, age)
      }
    }
  }

  override def getActorProps: Props = Props(classOf[Worker], this)
}
