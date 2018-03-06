package com.linehrr.akka.http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.name.Named
import com.linehrr.akka.http.handler.IFactory
import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration._

@Named("test")
class MainServer extends HttpApp {
  @Inject @Named("parser") var parserFactory: IFactory = null

  override protected def routes: Route = {
    val parseActorRef = parserFactory.get()

    path("auth") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>You are authenticated</h1><body>Just a joke...</body>"))
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


