package controllers

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import models.GameInstance.GetWebController
import models.GameInstance.WebControllerInfo
import models.WebControllerActor.UpdateWeb
import models.WebControllerActor.RegisterWebsocket
import models.WebControllerActor.UnregisterWebsocket
import models.WebControllerActor.GameOverWeb
import play.api.libs.json.JsValue

object WebSocketActor {
  def props(webSocketOut: ActorRef, controllerActor: ActorRef) =
    Props(new WebSocketActor(webSocketOut, controllerActor))
}

class WebSocketActor(socketOut: ActorRef, controllerActor: ActorRef) extends Actor {
  private implicit val ec: ExecutionContext = context.dispatcher


  val futureWuiRef = {
    implicit val to = Timeout(2, TimeUnit.SECONDS)
    (controllerActor ? GetWebController).mapTo[WebControllerInfo].map(_.wuiRef)
  }


  futureWuiRef.foreach(_ ! RegisterWebsocket(self))

  override def receive: Receive = {
    // to browser
    case UpdateWeb(json) => socketOut ! json
    case GameOverWeb(json) => socketOut ! json

    // from browser
    case json: JsValue =>
      println("browser send: " + json)

  }

  override def postStop() {
//    For DEBUG
//    println("Disconnected.")
    UnregisterWebsocket
    futureWuiRef.foreach(_ ! UnregisterWebsocket(self))
  }
}
