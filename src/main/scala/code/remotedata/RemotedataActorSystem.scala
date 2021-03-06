package code.remotedata

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import net.liftweb.util.Props
import net.liftweb.common.Loggable


object RemotedataActorSystem extends Loggable {

  var obpActorSystem: ActorSystem = null

  def init () = {
    if (obpActorSystem == null ) {
      val system = ActorSystem("LookupSystem", ConfigFactory.load(ConfigFactory.parseString(RemotedataConfig.lookupConf)))
      logger.info(RemotedataConfig.lookupConf)
      obpActorSystem = system
    }
    obpActorSystem
  }


  def getActor(actorName: String) = {
    this.init

    val actorPath = Props.getBool("remotedata.enable", false) match {
    case true =>
      val hostname = RemotedataConfig.remoteHostname 
      val port = RemotedataConfig.remotePort
      "akka.tcp://RemotedataActorSystem@" + hostname + ":" + port + "/user/" + actorName

    case false =>
      val hostname = RemotedataConfig.localHostname 
      val port = RemotedataConfig.localPort
      "akka.tcp://RemotedataActorSystem@" + hostname + ":" + port + "/user/" + actorName
    }

    this.obpActorSystem.actorSelection(actorPath)
  }

}

