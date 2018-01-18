package controllers

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.ExecutionContext

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import models.GameInstance.GetWebController
import models.GameInstance.WebControllerInfo
import play.api.libs.json.JsValue

object WebSocketActor {
  def props(webSocketOut: ActorRef, controllerActor: ActorRef) =
    Props(new WebSocketActor(webSocketOut, controllerActor))
}

class WebSocketActor(socketOut: ActorRef, controllerActor: ActorRef) extends Actor {
  private implicit val ec: ExecutionContext = context.dispatcher

  context.actorSelection(context.system.child("view$wui")) ! "Some message"

  val futureWuiRef = {
    implicit val to = Timeout(2, TimeUnit.SECONDS)
    (controllerActor ? GetWebController).mapTo[WebControllerInfo].map(_.wuiRef)
  }


  futureWuiRef.foreach(_ ! "Hello World")

  override def receive: Receive = {
    case s: String => println(s"ws actor received msg: $s")
    // from browser
    case json: JsValue =>
      // to browser
      println("to browser")
      socketOut ! json
  }

  override def postStop() {
    println("Disconnected.")
    //unsubscribe
  }
}
