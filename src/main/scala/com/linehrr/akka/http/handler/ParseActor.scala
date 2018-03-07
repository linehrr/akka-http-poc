package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.routing._
import com.google.inject.name.Named
import javax.inject.Inject
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

class ParseActor(factory: IFactory) extends Actor {

  val router: ActorRef = {
//    val routees = Vector.fill(20) {
//
//      val r = factory.get()
//      context watch r
//      ActorRefRoutee(r)
//    }

    context.actorOf(
      RoundRobinPool(
        30,
        Some(DefaultResizer(5, 80))
      ).props(Props(factory.getActor()))
    )

//    Router(SmallestMailboxRoutingLogic(), routees)
  }

  implicit val timeout: Timeout = Timeout(15 seconds)
  override def receive: Receive = {
    case (name: String, age: String) => {
      val f = router ask (name, age)
      val ret = Await.result(f, Duration.Inf).asInstanceOf[String]

      sender() ! ret
    }
//    case Terminated(actor) => {
//      router.removeRoutee(actor)
//      val r = factory.get()
//      context watch r
//      router.addRoutee(r)
//    }
  }
}

@Named("parser")
class ParserFactory extends IFactory {
  @Inject var system: ActorSystem = null

  @Inject @Named("worker") var workerFactory: IFactory = null

  override def get(): ActorRef = {
    system.actorOf(Props(classOf[ParseActor], workerFactory))
  }

  override def getActor(): Actor = {
    new ParseActor(workerFactory)
  }
}