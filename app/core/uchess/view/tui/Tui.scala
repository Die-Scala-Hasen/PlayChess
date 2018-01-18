package core.uchess.view.tui

import java.io.IOException

import akka.actor.Actor
import akka.actor.ActorSelection
import core.uchess.controller.impl.GameoverInfo
import core.uchess.controller.impl.UpdateInfo
import core.uchess.controller.impl.QuitCmd
import core.uchess.controller.impl.RestartCmd
import core.uchess.controller.impl.MoveCmd
import core.uchess.model.impl.GameField
import core.uchess.util.GameFieldPoint

class Tui extends Actor {

  private val controller: ActorSelection = context.system.actorSelection("user/controller")


  private val readThread = new Thread {
    override def run(): Unit = {
      try {
        while (true) {
          val input = scala.io.StdIn.readLine().trim()
          self ! input
        }
      } catch {
        case _: InterruptedException | _: IOException =>
      }
    }
  }


  override def preStart(): Unit = readThread.start()

  override def postStop(): Unit = readThread.interrupt()

  override def receive: Receive = {
    case info: GameoverInfo => printGameover(info)
    case info: UpdateInfo => printUpdate(info)
    case input: String => processInputLine(input)
  }

  def processInputLine(line: String) = {
    line.toLowerCase match {
      case "q" => controller ! QuitCmd
      case "r" => controller ! RestartCmd
      case select if line.startsWith("s") && line.length == 3 => handleMove(select)
      case move if line.startsWith("m") && line.length == 3 => handleMove(move)
      case inp => println(s"Invalid input!: $inp")
    }
  }

  def handleMove(line: String) = {
    try {
      val src = GameFieldPoint(line.charAt(1).toString, line.charAt(2).toString.toInt)
      controller ! MoveCmd(src)
    } catch {
      case _: NumberFormatException => println("Invalid command!")
    }
  }

  private def printGameover(info: GameoverInfo) = {
    printGameField(info.gameField)
    println(info.status)
    println("Please enter a command: q - quit, r - restart")
  }

  private def printUpdate(info: UpdateInfo) = {
    printGameField(info.gameField)
    println(info.status)
    println("Please enter a command: q - quit, r - restart, s+coordinates to select a figure (example: sc7), m+coordinates to move (example:mc6)")
  }

  def printGameField(gameField: GameField): Unit = {
    println(gameField.toString)
  }
}
