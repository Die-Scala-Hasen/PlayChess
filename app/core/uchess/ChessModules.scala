package core.uchess

import akka.actor.{ActorRef, ActorSystem, Props}
import core.uchess.view.gui.SwingActor
import core.uchess.view.tui.Tui

// Todo marcel was soll der java code hier????
trait ChessModule {
  def createUI()(implicit system: ActorSystem): Unit

  def createTUI()(implicit system: ActorSystem): ActorRef = system.actorOf(Props[Tui], "view$tui")

  def createGUI()(implicit system: ActorSystem): ActorRef = system.actorOf(Props[SwingActor], "view$gui")
}

trait OfflineModule extends ChessModule {
  var tui: ActorRef = _
  var gui: ActorRef = _

  override def createUI()(implicit system: ActorSystem): Unit = {
    tui = createTUI()
    gui = createGUI()
  }

  trait WebModule extends ChessModule {
    override def createUI()(implicit system: ActorSystem): Unit = {
      //createTUI()
    }

}


}