package com.linehrr.akka.http.handler

import akka.actor.ActorRef

trait IFactory {
  def get(): ActorRef
}
