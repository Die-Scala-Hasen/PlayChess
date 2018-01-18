package core.uchess

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, ActorSystem, Props}
import core.uchess.controller.impl.UChessController
import core.uchess.view.gui.SwingActor
import core.uchess.view.tui.Tui

class Main (implicit val system: ActorSystem) {

  private val tuiRef = system.actorOf(Props[Tui], "view$tui")
  private val guiRef = system.actorOf(Props[SwingActor], "view$gui")
  val controller: ActorRef = system.actorOf(UChessController.props(List(tuiRef, guiRef)), "controller")


}

object Main {

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("ChessSystem")
    val chess = new Main
    val cdl = new CountDownLatch(1)

    chess.system.registerOnTermination{cdl.countDown()}
    cdl.await()
    chess.system.whenTerminated.onComplete(_ => System.exit(0))
  }
}
