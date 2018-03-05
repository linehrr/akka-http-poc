package com.linehrr.akka.http.injector

import com.google.inject.AbstractModule
import com.linehrr.akka.http.handler.{IParser, ParameterParser}

class AppInjector extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[IParser]).to(classOf[ParameterParser])
  }
}
