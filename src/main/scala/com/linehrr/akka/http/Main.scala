package com.linehrr.akka.http

import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.google.inject.Key
import com.google.inject.name.Names
import com.linehrr.akka.http.injector.AppInjector
import com.typesafe.config.{Config, ConfigFactory}

object Main extends App {

  val serverConfig: Config = ConfigFactory.load("app.conf")
  val serverSettings = ServerSettings(serverConfig)

  val mainServer = AppInjector().getInstance(Key.get(classOf[HttpApp], Names.named("test")))

  mainServer.startServer("0.0.0.0", 8080, serverSettings)
}
