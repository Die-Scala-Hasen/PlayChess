package core.uchess.model.impl

import core.uchess.model.Piece
import core.uchess.util.Point

case class Knight(color: Char) extends Piece {

  override def possibleMove(gameField: Map[Point, Piece], currentPoint: Point): List[Point] = {
    val possibilities = Vector((1, 2), (-1, 2), (1, -2), (-1, -2), (2, 1), (2, -1), (-2, 1), (-2, -1))
    internalMoveSingleSteps(gameField, currentPoint, possibilities.toList)
  }

  override def toString: String = {
    color match {
      case 'w' => "wk"
      case 'b' => "bk"
      case _ => "Kn" + color
    }
  }

  override def figureDetails: String = {
    color+"Knight"
  }
}
