package services

import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.ActorRef
import akka.actor.ActorSelection
import akka.actor.ActorSystem

@Singleton
class ActorResolver @Inject()(implicit actorSystem: ActorSystem) {


  def resolveGameController(): ActorRef = {
    val gameController: ActorSelection = actorSystem.actorSelection("view$wui")
    Await.result(gameController.resolveOne(5.seconds), 5.seconds)
  }

}