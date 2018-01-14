package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import models.GameInstance
import play.api.mvc._


class Application @Inject()(implicit system: ActorSystem, materializer: Materializer) extends Controller {

 // private var gameInstances:GameInstance = new GameInstance()



  def chess() = Action {
    Ok(views.html.wui("Test"))
  }
}
