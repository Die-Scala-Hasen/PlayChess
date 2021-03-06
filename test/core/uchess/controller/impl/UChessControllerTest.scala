package core.uchess.controller.impl

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
import core.uchess.model.impl.GameField
import core.uchess.util.Point
import org.scalatest.WordSpec
import org.scalatest.Matchers

class UChessControllerTest extends WordSpec with Matchers {

  var testInfo: Info = _

  class TestUI extends Actor {
    override def receive: Receive = {
      case info: Info => testInfo = info
    }
  }

  "A new UChessController as part of an Actorsystem" should {

    implicit val system: ActorSystem = ActorSystem("UChessTestSystem")
    val testUiRef = system.actorOf(Props(new TestUI), "view$testUI")
    val controller: ActorRef = system.actorOf(UChessController.props(List(testUiRef)), "controller")

    "handle a invalid command" in {
      controller ! RestartCmd
      controller ! "Invalid Command String"
      Thread.sleep(1000) // wait for actor message receive

      testInfo match {
        case ii: InvalidInfo =>
          ii.message shouldBe "Invalid Command"
      }
    }

    "handle selecting a figure" in {

      controller ! RestartCmd
      controller ! MoveCmd(Point(4, 6))
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case ui: UpdateInfo =>
          ui.status shouldBe "One Figure is selected"
          ui.selfPos shouldBe Point(4, 6)
          ui.possibleMoves should contain(Point(4, 4))
          ui.possibleMoves should contain(Point(4, 5))
      }
    }

    "handle move a figure" in {

      controller ! RestartCmd
      controller ! MoveCmd(Point(4, 6))
      controller ! MoveCmd(Point(4, 4))
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case ui: UpdateInfo =>
          ui.status shouldBe "Figure was moved successfully."
          ui.selfPos shouldBe null
          ui.possibleMoves shouldBe null
      }
    }

    "handle the right players turn" in {

      controller ! RestartCmd

      controller ! MoveCmd(Point(4, 6))
      controller ! MoveCmd(Point(4, 4))
      Thread.sleep(200) // wait for actor message receive

      var gameFileAfterMove: GameField = null

      testInfo match {
        case ui: UpdateInfo =>
          gameFileAfterMove = ui.gameField
      }

      controller ! MoveCmd(Point(4, 4))
      Thread.sleep(200) // wait for actor message receive


      testInfo match {
        case ui: UpdateInfo =>
          ui.gameField shouldEqual gameFileAfterMove
      }
    }

    "handle if no figure is selected to move" in {

      controller ! RestartCmd
      controller ! MoveCmd(Point(4, 4))
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case ui: UpdateInfo =>
          ui.status shouldBe "No Figure selected."
          ui.selfPos shouldBe null
          ui.possibleMoves shouldBe null
      }
    }

    "handle an invalid move of figure" in {

      controller ! RestartCmd
      controller ! MoveCmd(Point(4, 6))
      controller ! MoveCmd(Point(4, 3))
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case ui: UpdateInfo =>
          ui.status shouldBe "Invalid move!"
          ui.selfPos shouldBe null
          ui.possibleMoves shouldBe null
      }
    }


    "handle game over for white" in {

      controller ! RestartCmd
      controller ! MoveCmd(Point(4, 6))
      controller ! MoveCmd(Point(4, 4))
      controller ! MoveCmd(Point(4, 1))
      controller ! MoveCmd(Point(4, 3))
      controller ! MoveCmd(Point(3, 7))
      controller ! MoveCmd(Point(6, 4))
      controller ! MoveCmd(Point(4, 0))
      controller ! MoveCmd(Point(4, 1))
      controller ! MoveCmd(Point(6, 4))
      controller ! MoveCmd(Point(6, 3))
      controller ! MoveCmd(Point(2, 1))
      controller ! MoveCmd(Point(2, 3))
      controller ! MoveCmd(Point(6, 3))
      controller ! MoveCmd(Point(4, 1))
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case gi: GameoverInfo =>
          gi.status shouldBe "White got the chicken dinner"
      }
    }


    "handle game over for balck" in {

      controller ! RestartCmd

      controller ! MoveCmd(Point(4, 6))
      controller ! MoveCmd(Point(4, 4))

      controller ! MoveCmd(Point(4, 1))
      controller ! MoveCmd(Point(4, 3))

      controller ! MoveCmd(Point(4, 7))
      controller ! MoveCmd(Point(4, 6))

      controller ! MoveCmd(Point(3, 0))
      controller ! MoveCmd(Point(6, 3))

      controller ! MoveCmd(Point(3, 6))
      controller ! MoveCmd(Point(3, 4))

      controller ! MoveCmd(Point(6, 3))
      controller ! MoveCmd(Point(6, 4))

      controller ! MoveCmd(Point(2, 6))
      controller ! MoveCmd(Point(2, 4))

      controller ! MoveCmd(Point(6, 4))

      controller ! MoveCmd(Point(4, 6))
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case gi: GameoverInfo =>
          gi.status shouldBe "Black got the chicken dinner"
      }
    }

    "restart game" in {
      controller ! RestartCmd
      Thread.sleep(200) // wait for actor message receive

      testInfo match {
        case ui: UpdateInfo =>
          ui.status shouldBe "Welcome to UChess"
          ui.selfPos shouldBe null
      }
    }

    "quit game" in {
      controller ! QuitCmd

      Thread.sleep(200) // wait for actor message receive
    }
  }
}
