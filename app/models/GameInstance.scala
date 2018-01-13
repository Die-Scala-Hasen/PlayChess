package models


import akka.actor.Actor
import akka.actor.Props
import core.uchess.Main
import core.uchess.OfflineModule
import core.uchess.controller.impl.UpdateInfo

class GameInstance() {

  class GameInstanceActor extends Actor {

    override def receive: Receive = {
            // update
      case info: UpdateInfo =>
        val gamefield = info.gameField
        println(gamefield.toString)
      println("TEST hallo Ficken")
      case _ => println("Penis dich ")
    }

  }


  val main = new Main with OfflineModule
  main.start()

  main.system.actorOf(Props(new GameInstanceActor()), "view$wui")

}