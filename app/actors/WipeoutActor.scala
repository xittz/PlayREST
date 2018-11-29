package actors

import akka.actor._
import play.api.Logger
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

import services._

@Singleton
class WipeoutActor @Inject() (service: UserService)(implicit ec: ExecutionContext) extends Actor {
  override def receive: Receive = {
      case "wipeout" => {
        service.deleteAll
        Logger.info("Users are wiped out")
      }
      case _ => Logger.warn("WipeoutActor recceiver an unknown message!")
  }
}

