

/**
 * @author joonardo
 */

//import scala.swing.event.Key.Value

import Settings._

class Player(val key : String) {
  var alive = true
  var died = 0L
  var character = new Character(this)
  private var keyDown = false
  GameObjectRoot.addGameObject(this.character)
  var score = 0
  
  def pressedTime : Option[Long] = {
    if(!this.keyDown && end > start){
      val res = end - start
      start = end
      Some(res)
    }else{
      None
    }
  }
  
  private var end : Long = System.currentTimeMillis()
  private var start : Long = System.currentTimeMillis()
  
  def keyPressed() = {
    this.character.shouldMove = true
    if(!this.keyDown){
      this.start = System.currentTimeMillis()
      this.keyDown = true
    }
  }
  
  def keyReleased() = {
    this.character.shouldMove = false
    this.character.left = !this.character.left
    this.end = System.currentTimeMillis()
    this.keyDown = false
    //println(this.pressedTime, this.start, this.end)
  }
  
  def tryRespawn() = {
    if(!this.alive && System.currentTimeMillis() - this.died > RespawnTime){
      this.character = new Character(this)
      GameObjectRoot.addGameObject(this.character)
      this.alive = true
      //this.end = System.currentTimeMillis() // Prevent kicking
    }
  }
  
  def initKick() = this.character.shouldBeRemoved = true//GameObjectRoot.gameObjs.foreach { x => if(x.owner == this) x }
  
  def hasBeenAFKTooLong = !this.keyDown && this.alive && System.currentTimeMillis() - this.end > KickThreshold/*{
    if(this.shouldUpdate){
      this.start = System.currentTimeMillis()
      this.shouldUpdate = false
    }
    if(System.currentTimeMillis() - this.start > KickThreshold){
      this.shouldUpdate = true
      true
    }else{
      false
    }
  }*/
  
  //def key = this.kh.key
}