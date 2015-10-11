

/**
 * @author joonardo
 */

import scala.collection.mutable.{Buffer, Map}
import scala.swing.event.Key.Value

object GameObjectRoot {
  var area = (0,0)
  //var initDone = false
  val gameObjs = Buffer[GameObject]()
  val players = Map[String, Player]()
  var lastUpdate = System.currentTimeMillis()
  var nextPowerupSpawnTime = Rng.nextPowerupSpawnTime
  //val keys = Buffer[Int]()
  
  def addGameObject(go : GameObject) = {
    this.gameObjs.append(go)
  }
  
  def areaWidth = this.area._2
  def areaHeight = this.area._1
  
  def apply(key : String) : Player = this.players(key)
  
  def addPlayer(p : Player) = {
    this.players += p.key -> p
  }
  
  def keys = this.players.keys.toBuffer
  
  def update() = {
    this.area = OneButtonGame.scr.getSize
    
    for(p <- this.players){
      p._2.tryRespawn()
    }
    
    var removed = Buffer[GameObject]()
    for (go <- this.gameObjs){
      go.update(dt)
      if(go.shouldBeRemoved){
       removed += go
      }
    }
    
    this.gameObjs --= removed
    
    if(System.currentTimeMillis() > this.nextPowerupSpawnTime){
      this.gameObjs += Powerup.random()
      this.nextPowerupSpawnTime = Rng.nextPowerupSpawnTime
    }
    
    this.lastUpdate = System.currentTimeMillis()
  }
  
  def dt = (System.currentTimeMillis() - this.lastUpdate)/1000.0
}

