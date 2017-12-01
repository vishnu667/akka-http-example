package com.example.handlers

import akka.actor.{Actor, ActorLogging, Props}
import com.example.connectors.RedisConnector
import akka.pattern.pipe

import scala.concurrent.ExecutionContextExecutor

object UserHandler {
  def props(db: RedisConnector): Props = Props(new UserHandler(db))

  case class User(username: String, details: String, id: Option[Long] = None, passwordHash: Option[String] = None)

  case class Register(username: String, password: String)

  case class Update(username: String, details: String)

  case class GetUser(username: String)

  case class DeleteUser(username: String)

  case class UserNotFound(username: String)

  case class UserDeleted(username: String)

}

class UserHandler(redisConnector: RedisConnector) extends Actor with ActorLogging {

  import com.example.handlers.UserHandler._

  implicit val ec: ExecutionContextExecutor = context.dispatcher


  override def receive: Receive = {

    case Register(id, pwd) =>
      redisConnector.upsert(id, pwd) pipeTo sender()

    case Update(id, details) =>
      redisConnector.upsert(id, details) pipeTo sender()

    case GetUser(userName) =>
      //closing over the sender in Future is not safe. http://helenaedelson.com/?p=879
      val requestor = sender()
      redisConnector.get(userName).foreach {
        case Some(i) => requestor ! User(userName, i)
        case None => requestor ! UserNotFound(userName)
      }

    case DeleteUser(userName) =>
      val requestor = sender()
      redisConnector.del(userName).foreach {
        case effectedRows if effectedRows > 0 =>
          requestor ! UserDeleted(userName)
        case _ => requestor ! UserNotFound(userName)
      }
  }
}

