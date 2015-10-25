

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
  private val infoTxts = Map[String, Text]("Caption" -> Text("Leader", 8, alpha = 0.5))
  //val keys = Buffer[Int]()
  
  def infoTexts = this.infoTxts.values.toBuffer
  
  def addGameObject(go : GameObject) = {
    this.gameObjs.append(go)
  }
  
  private def leaderText = {
    val leader = getLeader
    var txt = "Nobody"
    if(leader.isDefined){
      txt = leader.get.key + ": " + leader.get.score
    }
    Text(txt, 8, Some(this.infoTxts("Caption")), Vector.normal(0, 70), alpha = 0.5)
  }
  
  private def getLeader = {
    if(this.players.size > 0){
      val plas = this.players.values.toBuffer
      var leader = plas(0)
      plas.foreach { x => if(x.score > leader.score) leader = x }
      Some(leader)
    }else{
      None
    }
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
    this.infoTxts("Leader") = this.leaderText 
    
    for(p <- this.players){
      p._2.tryRespawn()
    }
    
    var removed = Buffer[GameObject]()
    var kickThese = Buffer[String]()
    for (go <- this.gameObjs){
      if(go.shouldBeRemoved){
       removed += go
      }else{
        go.update(dt)
        if(go.ownerOp.isDefined && go.ownerOp.get.hasBeenAFKTooLong){
          go.ownerOp.get.initKick()
          kickThese += go.ownerOp.get.key
        }
      }
    }
    
    this.gameObjs --= removed
    this.players --= kickThese
    
    if(System.currentTimeMillis() > this.nextPowerupSpawnTime){
      this.gameObjs += Powerup.random()
      this.nextPowerupSpawnTime = Rng.nextPowerupSpawnTime
    }
    
    this.lastUpdate = System.currentTimeMillis()
  }
  
  def dt = (System.currentTimeMillis() - this.lastUpdate)/1000.0
}

