package modules

import actors._
import akka.actor._

import com.google.inject.AbstractModule
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport
import scala.concurrent.ExecutionContext
import javax.inject.{Inject, Named}
import scala.concurrent.duration._

class NewUserScheduler @Inject() (val system: ActorSystem, @Named("NewUserActor") val receiver: ActorRef)
    (implicit ec: ExecutionContext) {
    var actor = system.scheduler.schedule(
        initialDelay = 1.minute,
        interval = 1.minute,
        receiver = receiver,
        message = "addNewUser"
    )
}

class NewUserModule extends AbstractModule with AkkaGuiceSupport {
    def configure() = {
        Logger.warn("[ Configuring Scheduler ]")
        bindActor[NewUserActor]("NewUserActor")
        bind(classOf[NewUserScheduler]).asEagerSingleton()
    }
}