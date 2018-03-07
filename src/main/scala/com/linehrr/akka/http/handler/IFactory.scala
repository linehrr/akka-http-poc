package com.linehrr.akka.http.handler

import akka.actor.{Actor, ActorRef}

trait IFactory {
  def get(): ActorRef

  def getActor(): Actor
}
