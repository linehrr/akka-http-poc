package com.linehrr.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.name.Named
import com.linehrr.akka.http.handler.ActorFactory
import com.twg.logic.auth.IAuth
import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration._

class MainServer extends HttpApp {

  @Inject var system: ActorSystem = _
  @Inject @Named("parser") var parser: ActorFactory = _
  @Inject var authMod:IAuth = _

  override protected def routes: Route = {
    val parseActorRef = system.actorOf(parser.getActorProps)

    path("auth") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>You are authenticated</h1><body>Just a joke...</body>"))
      }
      post {
        entity(as[String]) {
          json =>
            if(authMod.validateUser(json)){
              complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "auth ok"))
            }else{
              complete(StatusCodes.Forbidden -> "User auth failed")
            }
        }
      }
    } ~
    path("actor") {
      implicit val timeout: Timeout = Timeout(250 millisecond)
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


