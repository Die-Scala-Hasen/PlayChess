package core.uchess

import akka.actor.{ActorRef, ActorSystem, Props}
import com.google.inject.Inject
import core.uchess.controller.impl.UChessController

// TODO MARCEL JAVA CODE???
class Main (implicit val system: ActorSystem) {
  this: ChessModule =>
  var controller: ActorRef = null // TODO MARCEL Null is poop

  def start(): Unit = {
    createUI()
    controller = system.actorOf(UChessController.props(List(createGUI(), createTUI())), "controller")
  }
}

object Main {

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("ChessSystem")
    val chess = new Main with OfflineModule
    chess.start()
    chess.system.whenTerminated.onComplete(_ => System.exit(0))

    while (true) {
      val input = scala.io.StdIn.readLine()
      chess.tui ! input
    }
  }
}
