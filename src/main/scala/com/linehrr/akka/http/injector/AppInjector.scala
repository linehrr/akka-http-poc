package com.linehrr.akka.http.injector

import akka.actor.ActorSystem
import akka.http.scaladsl.server.HttpApp
import com.google.inject._
import com.google.inject.name.Names
import com.linehrr.akka.http.MainServer
import com.linehrr.akka.http.handler._
import com.twg.logic.auth.{IAuth, UserAuth}
import com.twg.logic.db.{IDB, RDS}
import com.twg.logic.handler.{IParameterParser, ParameterParser}

class AppInjector extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorSystem]).toInstance(ActorSystem("AppActor"))

    bind(classOf[ActorFactory])
      .annotatedWith(Names.named("parser-worker"))
      .to(classOf[WorkerFactory])

    bind(classOf[ActorFactory])
      .annotatedWith(Names.named("parser"))
      .to(classOf[ParserActorFactory])

    bind(classOf[HttpApp])
      .to(classOf[MainServer])

    bind(classOf[IParameterParser])
      .to(classOf[ParameterParser])

    bind(classOf[IDB])
      .to(classOf[RDS])

    bind(classOf[IAuth])
      .to(classOf[UserAuth])
  }
}

object AppInjector {
  val injector: Injector = Guice.createInjector(new AppInjector)
  def apply(): Injector = injector

  def getMainServer: HttpApp = injector.getInstance(classOf[HttpApp])

  def getParameterParser: IParameterParser = injector.getInstance(classOf[IParameterParser])
}