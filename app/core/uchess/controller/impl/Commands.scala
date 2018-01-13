package core.uchess.controller.impl

import core.uchess.util.Point

trait Command {}

case object QuitCmd extends Command
case object RestartCmd extends Command
case class MoveCmd(point: Point) extends Command
