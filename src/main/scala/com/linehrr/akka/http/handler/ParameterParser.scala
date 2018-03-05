package com.linehrr.akka.http.handler

import com.google.inject.Singleton

@Singleton class ParameterParser extends IParser {
  override def parse(name: String, age: String): String = {
    s"name: $name, age: $age"
  }
}
