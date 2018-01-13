package core.uchess.controller

import core.uchess.model.impl.GameField
import core.uchess.util.Point

trait Controller {
  def getField(): GameField

  def doMove(target: Point): Unit

  def checkWin(): Unit

  def reset(): Unit

  def exitGame(): Unit
}
