package com.example.routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.example.routes.api.ApiRoutes

trait BaseRoute extends ApiRoutes {

  def baseRoute = path("hello") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    }
  }~pathPrefix("api"){
    apiRoutes
  }~ path("register") {
    registerRoute
  }~ complete(StatusCodes.NotFound ->"Invalid Route")

}
