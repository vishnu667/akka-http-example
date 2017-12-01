package com.example.routes.api

import akka.pattern.ask
import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, OPTIONS, POST, PUT}
import akka.http.scaladsl.model.headers.Allow
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.example.handlers.UserHandler._
import com.example.routes.RouteHelper
import com.example.utils.json.JsonSupport
import spray.json.JsonParser.ParsingException
import spray.json._

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait UserRoute extends RouteHelper with JsonSupport{

  def userHandler: ActorRef

  def userRoute: Route = enableCORS {
    get {
      getUser
    } ~ put {
      complete("Put User")
    } ~ delete {
      complete("Deleting User")
    } ~ options {
      respondWithHeader(Allow(GET, OPTIONS, DELETE, PUT)) {
        complete(OK -> "OK")
      }
    } ~ respondWithHeader(Allow(GET, OPTIONS, DELETE, PUT)) {
      complete(METHOD_NOT_ALLOWED)
    }
  }

  def registerRoute: Route = get {
    complete(OK -> "Register Get")
  } ~ post {

    entity(as[String]) {
      body => try {
        body.parseJson match {
          case v:JsObject => {
            val user = v.convertTo[User]
            user.toJson
            val future = userHandler ? Register(user.username,user.details)
            onComplete(future) {
              case Success(s)=> s match {
                case v:Boolean => if(v) complete("Successful") else complete("False")
                case _ => complete(BAD_REQUEST("Internal server error"))
              }complete("Registration Successful")
              case Failure(f) => complete("Registration failed")
            }
//            complete(user.username+", "+user.details)
          }
          case _ => complete(BAD_REQUEST("Invalid Json Format"))
        }
      }catch {
        case _:DeserializationException => complete(BAD_REQUEST("Invalid JSON Format"))
        case _:ParsingException => complete(BAD_REQUEST("Invalid JSON"))
      }
    }
//    complete(OK -> "Register Post")
  } ~ respondWithHeader(Allow(GET, POST)) {
    complete(METHOD_NOT_ALLOWED)
  }


  private def getUser: Route = parameter("userName") {
    userName => {
      val future: Future[Any] = userHandler ? GetUser(userName)
      onComplete(future) {
        case Success(value) => value match {
          case u: User => complete("success got user " + u.username+" : "+u.details)
          case u: UserNotFound => complete("user Not found " + u.username)
        }
        case Failure(ex) => complete("Failed " + ex.getLocalizedMessage)
      }
    }
  } ~ complete(BAD_REQUEST("Pass User Name"))

}
