package models

import scala.collection.mutable.ListBuffer

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import core.uchess.controller.impl.UpdateInfo

class WebControllerActor extends Actor {
  val actorList = new ListBuffer[ActorRef]()

  override def receive: Receive = {
    case s: String =>
      actorList += sender
      println("new socket subscriber")
      sender ! "welcome socket"
    case info: UpdateInfo =>
      val gamefield = info.gameField
      for(actor <- actorList) {
        actor ! gamefield.toJson
      }
  }
}


object WebControllerActor{
  def props(): Props = Props(new WebControllerActor)

}
