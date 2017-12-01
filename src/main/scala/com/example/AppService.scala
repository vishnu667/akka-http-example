package com.example

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.{FormData, Multipart}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.example.connectors.RedisConnector
import com.example.handlers.UserHandler
import com.example.routes.BaseRoute
import redis.RedisClient
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor

trait AppService extends BaseRoute {

  import scala.concurrent.duration._

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  implicit def requestTimeout:Timeout = Timeout(5.seconds)

  protected[this] val route: Route = enableCORS{path("test"){
    enableCORS {
      post {
        complete("Not a multipart form data")
      } ~ complete("Send post request")
    }
  }
  }~ baseRoute
  private lazy val redis: RedisConnector = new RedisConnector {
    override val db: RedisClient = RedisClient(host = "localhost", port = 16379, db = Some(1))
  }

  implicit val userHandler: ActorRef = system.actorOf(UserHandler.props(redis))

}
