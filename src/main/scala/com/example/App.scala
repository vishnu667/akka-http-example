package com.example

import akka.http.scaladsl.Http

import scala.concurrent.Future

object App extends AppService {

  def main(args: Array[String]): Unit = {
    println("Starting Application")

    val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 8080)


    println(s"Server online at http://localhost:8080/")

    //    holdServer(bindingFuture)

    println("Exiting Main")
  }

  private def holdServer(bindingFuture: Future[Http.ServerBinding]): Unit = {
    println("Press RETURN to stop...")
    scala.io.StdIn.readLine()
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }


}
