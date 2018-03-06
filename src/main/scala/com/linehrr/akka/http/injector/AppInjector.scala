package com.linehrr.akka.http.injector

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Guice, Injector, Provides}
import com.linehrr.akka.http.handler._

class AppInjector extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorSystem]).toInstance(ActorSystem("AppActor"))

    bind(classOf[Factory])
      .annotatedWith(Names.named("worker"))
      .to(classOf[WorkerFactory])

    bind(classOf[Factory])
      .annotatedWith(Names.named("parser"))
      .to(classOf[ParserFactory])
  }

//  @Provides
//  @Named("ParserActorWorker")
//  def getParserActorWorker(system: ActorSystem): ActorRef = {
//    system.actorOf(Props(classOf[Worker]))
//  }
}

object AppInjector {
  val injector = Guice.createInjector(new AppInjector)
  def apply(): Injector = injector
}