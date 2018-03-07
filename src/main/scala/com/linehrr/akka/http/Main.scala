package com.linehrr.akka.http

import akka.http.scaladsl.settings.ServerSettings
import com.linehrr.akka.http.injector.AppInjector
import com.typesafe.config.{Config, ConfigFactory}

object Main extends App {

  val serverConfig: Config = ConfigFactory.load("app.conf")
  val serverSettings = ServerSettings(serverConfig)

  val mainServer = AppInjector.getMainServer

  mainServer.startServer("0.0.0.0", 8080, serverSettings)
}
