package com.example.connectors

import redis.{ByteStringSerializer, RedisClient}

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration.Duration

trait RedisConnector {
  val db: RedisClient

  def del(key: String): Future[Long] = db.del(key)

  def upsert[V](
                 key: String,
                 value: V,
                 expire: Option[Duration] = None
               )(implicit ev: ByteStringSerializer[V]): Future[Boolean] = expire match {
    case Some(duration) => db.set(key, value, Some(duration.toSeconds))
    case None => db.set(key, value)
  }

  def get(key: String): Future[Option[String]] = db.get[String](key)

}

object RedisConnector {
  object DB {
    val USERS_DB: Int = 1
    val URL_DB:Int = 2
    val LOOKUP_DB: Int = 3
    val COUNT_DB: Int = 4
  }
}