package com.linehrr.akka.http.handler

import akka.actor.Props

trait ActorFactory {
  def getActorProps: Props
}
