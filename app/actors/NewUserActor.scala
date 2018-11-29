package actors

import akka.actor._
import play.api.Logger
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.util.Random

import services._
import models._

@Singleton
class NewUserActor @Inject() (service: UserService)(implicit ec: ExecutionContext) extends Actor {
  override def receive: Receive = {
      case "addNewUser" => {
        val username = Random.alphanumeric.take(8).mkString("")
        val password = Random.alphanumeric.take(8).mkString("")
        val newUser = User(username = username, password = password)
        service.create(newUser)
        Logger.info("Added new user")
      }
      case _ => Logger.warn("NewUserActor recceived an unknown message!")
  }
}

