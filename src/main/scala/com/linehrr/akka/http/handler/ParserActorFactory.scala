package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.routing._
import akka.util.Timeout
import com.google.inject.Singleton
import com.google.inject.name.Named
import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
@Named("parser")
class ParserActorFactory extends ActorFactory {

  @Inject @Named("parser-worker") var worker: ActorFactory = _
  class ParseActor extends Actor {


    val router: ActorRef = {
      context.actorOf(
        RoundRobinPool(
          30,
          Some(DefaultResizer(5, 80))
        ).props(worker.getActorProps)
      )
    }

    implicit val timeout: Timeout = Timeout(150 millisecond)

    override def receive: Receive = {
      case (name: String, age: String) => {
        val f = router ask(name, age)
        val ret = Await.result(f, Duration.Inf).asInstanceOf[String]

        sender() ! ret
      }
    }
  }

  override def getActorProps: Props = Props(classOf[ParseActor], this)
}

