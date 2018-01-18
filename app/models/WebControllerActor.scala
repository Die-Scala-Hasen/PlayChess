package models

import scala.collection.mutable

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import core.uchess.controller.impl.UpdateInfo
import models.WebControllerActor.RegisterWebsocket
import models.WebControllerActor.UnregisterWebsocket
import models.WebControllerActor.UpdateWeb
import play.api.libs.json.JsValue

class WebControllerActor extends Actor {
  val actorList = new mutable.ListBuffer[ActorRef]()

  var lastInfo: Option[UpdateWeb] = None

  override def receive: Receive = {
    case RegisterWebsocket(socket) =>
      actorList.+=(socket)
      lastInfo.foreach{sender ! _}

    case UnregisterWebsocket(socket) => actorList.-=(socket)

    case info: UpdateInfo =>
      val gamefield = info.gameField
      val update = UpdateWeb(gamefield.toJson)
      lastInfo = Some(update)
      for(actor <- actorList) {
        actor ! update
      }
  }
}


object WebControllerActor{
  def props(): Props = Props(new WebControllerActor)

  case class RegisterWebsocket(actorRef: ActorRef)
  case class UnregisterWebsocket(actorRef: ActorRef)

  case class UpdateWeb(json: JsValue)


}
