package models

import scala.collection.mutable

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import core.uchess.controller.impl.UpdateInfo
import core.uchess.controller.impl.GameoverInfo
import models.WebControllerActor.RegisterWebsocket
import models.WebControllerActor.UnregisterWebsocket
import models.WebControllerActor.UpdateWeb
import models.WebControllerActor.GameOverWeb
import play.api.libs.json.JsValue
import play.api.libs.json.Json

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

    case gameOver: GameoverInfo =>
      val status:String = gameOver.status

      val sb: mutable.StringBuilder = new StringBuilder

      sb ++= "{\n" + "\"status\" : \""
      sb ++= status
      sb ++= "\"\n}"

      val statusAsJsonObject : JsValue = Json.parse(sb.result())
      val updateGameOver = GameOverWeb(statusAsJsonObject)
      for(actor <- actorList){
        actor ! updateGameOver
      }
  }
}


object WebControllerActor{
  def props(): Props = Props(new WebControllerActor)

  case class RegisterWebsocket(actorRef: ActorRef)
  case class UnregisterWebsocket(actorRef: ActorRef)

  case class UpdateWeb(json: JsValue)

  case class GameOverWeb(json: JsValue)

}
