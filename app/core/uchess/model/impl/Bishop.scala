package core.uchess.model.impl

import core.uchess.model.Piece
import core.uchess.util.Point

case class Bishop(color: Char) extends Piece {

  override def possibleMove(gameField: Map[Point, Piece], currentPoint: Point): List[Point] = {
    val possibilities = Vector((1, 1), (1, -1), (-1, 1), (-1, -1))
    internalMove(gameField, currentPoint, possibilities.toList)
  }

  override def toString: String = {
    color match {
      case 'w' => "♗"
      case 'b' => "♝"
      case _ => "B" + color
    }
  }
}
