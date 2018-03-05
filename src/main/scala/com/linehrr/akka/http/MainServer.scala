package com.linehrr.akka.http

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.name.Names
import com.google.inject.{Guice, Injector, Key}
import com.linehrr.akka.http.handler.ParseActor
import com.linehrr.akka.http.injector.AppInjector

import scala.concurrent.Await
import scala.concurrent.duration._

object MainServer extends HttpApp {
  override protected def routes: Route = {

    val injector: Injector = Guice.createInjector(new AppInjector)

    val system: ActorSystem = injector.getInstance(classOf[ActorSystem])
    val parseActorRef = system.actorOf(Props[ParseActor])

    path("test") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    } ~
    path("actor") {
      implicit val timeout: Timeout = Timeout(15 seconds)
      get {
        parameters('name, 'age) {
          (name, age) => {
            val response = parseActorRef ? ((name, age))

            val ret: String = Await.result(response, Duration.Inf).asInstanceOf[String]

            complete(
              HttpEntity(
                ContentTypes.`text/html(UTF-8)`,
                ret
              )
            )
          }
        }
      }
    }
  }
}


