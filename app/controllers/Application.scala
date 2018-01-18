package controllers

import akka.actor.Props
import akka.actor.ActorSystem
import akka.actor.ActorRefFactory
import akka.stream.Materializer
import com.google.inject.Inject
import com.google.inject.Singleton
import models.GameInstance
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._

@Singleton
class Application @Inject()(gameInstance: GameInstance)
  (implicit system: ActorSystem, materializer: Materializer) extends Controller {

  def socket = WebSocket.accept[JsValue, JsValue] { requestHeader =>
    ActorFlow.actorRef[JsValue, JsValue] { outref => WebSocketActor.props(outref, gameInstance.gameInstanceActorRef)
    }
  }


  def chess() = Action {
    Ok(views.html.wui("test"))
  }
}


