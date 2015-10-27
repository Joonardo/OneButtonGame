

/**
 * @author joonardo
 */

import scala.swing.Graphics2D
import Utils._
import Settings._

abstract class Bullet(owner : Player, val dir : Double, var position : Vector) extends GameObject(Some(owner)) {
  val _lifetime : Long
  private val born = System.currentTimeMillis()
  val _damage : Int
  val _recoil : Int
  var shouldMove = true
  
  GameObjectRoot.addGameObject(this)
  
  def recoil = this.velocity.unit*this._recoil
  
  override def paint(g : Graphics2D) = {
    g.drawLine(this.x, this.y, toInt(this.x-10*this.velocity.unit.x), toInt(this.y-10*this.velocity.unit.y))
  }
  
  override def update(dt : Double) = {
    if(System.currentTimeMillis() - this.born > this._lifetime){
      this.destroy(this.owner)
    }else{
      this.position = this.position + this.velocity*dt
      GameObjectRoot.gameObjs.foreach { x => {
        if(x.isInstanceOf[Hittable]){
          val c = x.asInstanceOf[Hittable] 
          if((this.position - c.position).abs <= c.radius){
            c.takeHit(this._damage)
            c.velocity += recoil
            if(c.health <= 0 && !c.shouldBeRemoved){
              c.destroy(this.owner)
            }
            this.destroy(this.owner)
          }
        }
      }}
    }
    checkCollision()
  }
}

abstract class Weapon(owner : Player) {
  var ammo = Ammo(this.getClass)
  val shootingThreshold = 200L
  def holder = owner.character
  def fire() : Unit
  def checkTrigger() = {
    this.shootingThreshold > this.owner.pressedTime.getOrElse(1000L)
  }
}