package core.uchess.controller.impl

import akka.actor.{Actor, ActorSelection}
import akka.actor.ActorRef
import akka.actor.Props
import core.uchess.controller.Controller
import core.uchess.model.Piece
import core.uchess.model.impl.GameField
import core.uchess.model.impl.Pawn
import core.uchess.util.Point

case object StartMessage

object UChessController {
  def props(viewRefs: List[ActorRef]): Props = Props(new UChessController(viewRefs))
}

class UChessController(viewRefs: List[ActorRef]) extends Actor with Controller {
  println("Hello World!")
  private var gameField = GameField(8)
  private var movePiece: Piece = null // TODO MARCEL Null is poop; _ == null
  private var movePiecePos: Point = null // TODO MARCEL Null is poop
  private var startPlayerWhite = true
  private var whiteKingAlive = true
  private var blackKingAlive = true
  private var status = ""
  private var selected = false
  private var gameOver = false
  private var winner = ""

  initAll()
  notifyView()

  private def tellView(msg: Any) = {
    viewRefs.foreach{view => view ! msg}
  }

  override def receive = {
    case QuitCmd => exitGame()
    case RestartCmd => reset()
    case move: MoveCmd => handleMovement(move.point)
    case _ if !winner.isEmpty => tellView(InvalidInfo(gameField, "Invalid Command"))
    case _ => tellView(InvalidInfo(gameField, "Invalid Command"))
  }

  private def initAll(): Unit = {
    gameField = GameField(8)
    movePiece = null
    movePiecePos = null
    startPlayerWhite = true
    whiteKingAlive = true
    blackKingAlive = true
    selected = false
    gameOver = false
    status = "Welcome to UChess"
    winner = ""
  }

  private def notifyView(): Unit = {
    println(gameField.toJson)
    if (gameOver) {
      val info = GameoverInfo(gameField, winner)
//      gameOver = false
      tellView(info)
    } else {
      var selPos: (Point) = null
      var possMoves: List[Point] = null
      if (selected) {
        selPos = movePiecePos
        possMoves = movePiece.possibleMove(gameField.gameField,selPos)
      }
      val info = UpdateInfo(gameField, possMoves, selPos, status)
      //remote ! info
      tellView(info)
    }

  }

  private def checkPlayerTurn(p: Piece): Boolean = {
    if (p.color.equals('w') && startPlayerWhite) {
      true
    } else if (p.color.equals('b') && !startPlayerWhite) {
      true
    }
    else
      false
  }

  override def getField(): GameField = {
    gameField
  }

  private def select(point: Point): Unit = {
    val gameField = this.gameField.gameField
    gameField.get(point) match {
      case None =>
        status = "No Figure selected."
        notifyView()
      case Some(p) =>
        if (checkPlayerTurn(p)) {
          movePiece = p
          movePiecePos = point
          selected = true
          status = "One Figure is selected"
          notifyView()
        }
    }
  }

  private def handleMovement(point: Point): Unit = {
    if (winner.nonEmpty) {
      gameOver = true
    }

    if (!selected) {
      select(point)
    } else {
      doMove(point)
    }
  }

  override def doMove(target: Point): Unit = {
    val start = movePiecePos
    val gameField = this.gameField.gameField
    gameField.get(start) match {
      case None =>
        status = "No Figure to move."
        notifyView()
      case Some(p) =>
        val validMoveList = p.possibleMove(gameField, start)
        if (validMoveList.contains(target) && checkPlayerTurn(p)) {
          turnColorChange()
          movePiece(p, start, target)
          status = "Figure was moved successfully."
        } else {
          status = "Invalid move!"
        }
        selected = false
        notifyView()
    }
  }

  private def turnColorChange() = {
    startPlayerWhite = !startPlayerWhite
  }

  private def movePiece(piece: Piece, start: Point, target: Point): Unit = {
    piece match {
      case pawn: Pawn =>
        pawn.firstMove = false
        gameField = gameField.copy(gameField = gameField.gameField - start + (target -> piece))
        checkWin()
      case _ =>
        gameField = gameField.copy(gameField = gameField.gameField - start + (target -> piece))
        checkWin()
    }
  }

  override def checkWin(): Unit = {
    whiteKingAlive = false
    blackKingAlive = false


    gameField.gameField.foreach { keyVal =>
      if (keyVal._2.toString.equals("♔")) {
        whiteKingAlive = true
      }
      if (keyVal._2.toString.equals("♚")) {
        blackKingAlive = true
      }
    }

    if (!whiteKingAlive) {
      gameOver = true
      winner = "Black got the chicken dinner"
    }

    if (!blackKingAlive) {
      gameOver = true
      winner = "White got the chicken dinner"
    }
  }

  override def reset(): Unit = {
    initAll()
    notifyView()
  }

  override def exitGame(): Unit = {
    context.system.terminate()
  }

}

