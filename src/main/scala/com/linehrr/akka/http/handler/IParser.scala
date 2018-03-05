package com.linehrr.akka.http.handler

import com.google.inject.{Inject, Singleton}

trait IParser {
  @Inject
  def parse(name: String, age: String): String
}
