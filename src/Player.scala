

/**
 * @author joonardo
 */

import scala.swing.event.Key.Value

class Player(val key : Value) {
  var alive = true
  var died = 0L
  val respawnTime = 5000 //5s
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
  
  private var start : Long = 0
  private var end : Long = 0
  
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
    if(!this.alive && System.currentTimeMillis() - this.died > this.respawnTime){
      this.character = new Character(this)
      GameObjectRoot.addGameObject(this.character)
      this.alive = true
    }
  }
  
  //def key = this.kh.key
}