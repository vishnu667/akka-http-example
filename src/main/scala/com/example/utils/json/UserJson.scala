package com.example.utils.json

import com.example.handlers.UserHandler.User
import spray.json.DefaultJsonProtocol

trait UserJson extends DefaultJsonProtocol{
  implicit val userFormat = jsonFormat4(User)
}
