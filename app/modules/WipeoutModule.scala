package modules

import actors._
import akka.actor._

import com.google.inject.AbstractModule
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport
import scala.concurrent.ExecutionContext
import javax.inject.{Inject, Named}
import scala.concurrent.duration._

class WipeoutScheduler @Inject() (val system: ActorSystem, @Named("WipeoutActor") val receiver: ActorRef)
    (implicit ec: ExecutionContext) {
    var actor = system.scheduler.schedule(
        initialDelay = 5.minutes,
        interval = 5.minutes,
        receiver = receiver,
        message = "wipeout"
    )
}

class WipeoutModule extends AbstractModule with AkkaGuiceSupport {
    def configure() = {
        Logger.warn("[ Configurring Scheduler ]")
        bindActor[WipeoutActor]("WipeoutActor")
        bind(classOf[WipeoutScheduler]).asEagerSingleton()
    }
}