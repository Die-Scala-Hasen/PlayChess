package models


import javax.inject.Singleton
import javax.inject.Inject

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import core.uchess.controller.impl.UpdateInfo
import core.uchess.controller.impl.UChessController
import core.uchess.view.gui.SwingActor
import core.uchess.view.tui.Tui
import models.GameInstance.GetWebController
import models.GameInstance.WebControllerInfo

@Singleton
class GameInstance @Inject()(implicit val system: ActorSystem) {

  class GameInstanceActor extends Actor {

    private val tuiRef = system.actorOf(Props[Tui], "view$tui")
    private val guiRef = system.actorOf(Props[SwingActor], "view$gui")
    private val wuiRef = system.actorOf(WebControllerActor.props(), "view$wui")
    val controller: ActorRef = system.actorOf(UChessController.props(List(tuiRef, guiRef, wuiRef)), "controller")


    override def receive: Receive = {
      // update
      case GetWebController => sender ! WebControllerInfo(wuiRef)
      case msg: String => println(msg + " nachricht war das ")
      case _ => println(" Message received from GameInstanceActor")
    }

    override def postStop() {
      println("Disconnected.")
    }

  }

  val gameInstanceActorRef: ActorRef = system.actorOf(Props(new GameInstanceActor()), "gameInstance")


}

object GameInstance {

  case object GetWebController
  case class WebControllerInfo(wuiRef: ActorRef)

}