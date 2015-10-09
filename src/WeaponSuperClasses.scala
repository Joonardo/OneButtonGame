

/**
 * @author joonardo
 */

import scala.swing.Graphics2D

class Bullet(owner : Player, val dir : Double, var position : Vector) extends GameObject(owner) {
  private var lifetime = 2500L
  private val born = System.currentTimeMillis()
  private var damage = 10
  private var recFactor = 45.0
  private var velFactor = 350.0
  var timeAlive = 0
  var shouldMove = true
  velocity = Vector.polar(dir, this.velFactor) + this.owner.character.velocity
  
  def recoil = this.velocity.unit*this.recFactor
  
  def applyRecoil() = this.owner.character.velocity -= recoil
  
  def setWeaponAttr(lt : Long = 2500, dmg : Int = 10, velF : Double = 350.0, rec : Double = 45) = {
    this.velFactor = velF
    velocity = Vector.polar(dir, this.velFactor) + this.owner.character.velocity
    this.lifetime = lt
    this.damage = dmg
    this.recFactor = rec
  }
  
  override def paint(g : Graphics2D) = {
    g.drawLine(this.x, this.y, toInt(this.x-10*this.velocity.unit.x), toInt(this.y-10*this.velocity.unit.y))
    //g.drawLine(this.x, this.y, toInt(this.x-10*cos(this.dir)), toInt(this.y-10*sin(this.dir)))
  }
  
  override def update(dt : Double) = {
    if(System.currentTimeMillis() - this.born > this.lifetime){
      this.destroy()
    }else{
      this.position = this.position + this.velocity*dt
      GameObjectRoot.gameObjs.foreach { x => {
        if(x.isInstanceOf[Hittable]){
          val c = x.asInstanceOf[Hittable] 
          if((this.position - c.position).abs <= c.radius){
            c.takeHit(this.damage)
            c.velocity += recoil
            if(c.health <= 0){
              c.destroy()
            }
            this.destroy()
          }
        }
      }}
    }
    checkCollision()
  }
}

abstract class Weapon(owner : Player) {
  var ammo : Int
  def holder = owner.character
  def fire() : Unit
}