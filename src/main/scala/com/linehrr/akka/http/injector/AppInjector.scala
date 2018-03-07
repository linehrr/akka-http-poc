package com.linehrr.akka.http.injector

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.HttpApp
import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Guice, Injector, Provides}
import com.linehrr.akka.http.MainServer
import com.linehrr.akka.http.handler._
import com.twg.logic.{IParameterParser, ParameterParser}

class AppInjector extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorSystem]).toInstance(ActorSystem("AppActor"))

    bind(classOf[IFactory])
      .annotatedWith(Names.named("worker"))
      .to(classOf[WorkerFactory])

    bind(classOf[IFactory])
      .annotatedWith(Names.named("parser"))
      .to(classOf[ParserFactory])

    bind(classOf[HttpApp])
      .annotatedWith(Names.named("test"))
      .to(classOf[MainServer])

    bind(classOf[Actor])
      .annotatedWith(Names.named("worker-actor"))
      .to(classOf[Worker])

    bind(classOf[IParameterParser])
      .to(classOf[ParameterParser])
  }

//  @Provides
//  @Named("ParserActorWorker")
//  def getParserActorWorker(system: ActorSystem): ActorRef = {
//    system.actorOf(Props(classOf[Worker]))
//  }
}

object AppInjector {
  val injector: Injector = Guice.createInjector(new AppInjector)
  def apply(): Injector = injector
}