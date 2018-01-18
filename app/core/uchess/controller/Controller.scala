package core.uchess.controller

import core.uchess.util.Point

trait Controller {
  def doMove(target: Point): Unit

  def checkWin(): Unit

  def reset(): Unit

  def exitGame(): Unit
}
