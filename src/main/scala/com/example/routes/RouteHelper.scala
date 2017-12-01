package com.example.routes

import akka.http.scaladsl.model.{StatusCode,StatusCodes}
import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Origin`}
import akka.util.Timeout
import spray.json.{JsBoolean, JsObject, JsString}

trait RouteHelper{

  protected implicit def ec: ExecutionContextExecutor

  lazy val METHOD_NOT_ALLOWED: (StatusCode, String) = StatusCodes.MethodNotAllowed -> JsObject("status" -> JsBoolean(false), "message" -> JsString("Unsupported Method")).prettyPrint

  lazy val OK:StatusCode = StatusCodes.OK

  def BAD_REQUEST(message:String): (StatusCode, String) = StatusCodes.BadRequest -> JsObject("status" -> JsBoolean(false), "message" -> JsString(message)).prettyPrint

  protected implicit val timeout: Timeout = Timeout(5.seconds)


  def enableCORS: Directive0 = {
    respondWithHeaders(
      `Access-Control-Allow-Origin`.*,
      `Access-Control-Allow-Credentials`(true),
      `Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With", "Allow")
    )
  }

}
