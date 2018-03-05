package com.linehrr.akka.http.handler

import akka.actor.ActorRef

trait Factory {
  def get(): ActorRef
}
