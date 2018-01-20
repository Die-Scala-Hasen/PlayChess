package core.uchess.model.impl

import core.uchess.model.Piece
import core.uchess.util.Point

case class King(color: Char) extends Piece {

  override def possibleMove(gameField: Map[Point, Piece], currentPoint: Point): List[Point] = {
    val possibilities = Vector((0, 1), (0, -1), (1, 0), (-1, 0), (1, 1), (1, -1), (-1, 1), (-1, -1))
    internalMoveSingleSteps(gameField, currentPoint, possibilities.toList)
  }

  override def toString: String = {
    color match {
      case 'w' => "wK"
      case 'b' => "bK"
      case _ => "K" + color
    }
  }

  override def figureDetails: String = {
    color+"King"
  }
}
