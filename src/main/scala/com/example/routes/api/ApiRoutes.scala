package com.example.routes.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.example.routes.RouteHelper

trait ApiRoutes extends RouteHelper with UserRoute {

  def apiRoutes: Route = path("user") {
    userRoute
  }
}
