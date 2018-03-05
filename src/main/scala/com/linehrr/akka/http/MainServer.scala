package com.linehrr.akka.http

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.linehrr.akka.http.handler.ParseActor

import scala.concurrent.Await
import scala.concurrent.duration._

object MainServer extends HttpApp {
  override protected def routes: Route = {

    val parseActorRef = ActorSystem("ParserSystem").actorOf(Props(new ParseActor), "parser")

    path("test") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    } ~
    path("actor") {
      implicit val timeout: Timeout = Timeout(100 milli)
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


