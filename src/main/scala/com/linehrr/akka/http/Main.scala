package com.linehrr.akka.http

import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.{Config, ConfigFactory}

object Main extends App {

  val serverConfig: Config = ConfigFactory.load("app.conf")
  val serverSettings = ServerSettings(serverConfig)

  MainServer.startServer("0.0.0.0", 8080, serverSettings)
}
