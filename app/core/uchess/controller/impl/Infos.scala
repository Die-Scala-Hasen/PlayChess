package core.uchess.controller.impl

import core.uchess.model.impl.GameField
import core.uchess.util.Point


trait Info {
  val gameField: GameField
}

case class InvalidInfo(gameField: GameField, message: String) extends Info

case class GameoverInfo(gameField: GameField, status: String) extends Info

case class UpdateInfo(gameField: GameField, possibleMoves: List[Point], selfPos: Point, status: String) extends Info
