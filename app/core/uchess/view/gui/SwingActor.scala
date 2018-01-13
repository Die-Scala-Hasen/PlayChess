package core.uchess.view.gui

import akka.actor.{Actor, ActorSelection}
import core.uchess.controller.impl.InvalidInfo
import core.uchess.controller.impl.Info

class SwingActor extends Actor {
  val controller: ActorSelection = context.system.actorSelection("user/controller")
  val frame = new SwingFrame(controller)

  override def receive: Receive = {
    case info: InvalidInfo => println(info.message)
    case info: Info => frame.update(info)
  }
}
