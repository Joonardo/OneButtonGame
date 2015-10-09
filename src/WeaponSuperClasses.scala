

/**
 * @author joonardo
 */

import scala.swing.Graphics2D

class Bullet(owner : Player, val dir : Double, var position : Vector) extends GameObject(owner) {
  val lifetime = 2500
  private val born = System.currentTimeMillis()
  private val damage = 10
  var timeAlive = 0
  var shouldMove = true
  velocity = Vector.polar(dir, 350) + this.owner.character.velocity
  
  def recoil = this.velocity.unit*45
  
  this.owner.character.velocity -= recoil
  
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

abstract class Weapon(owner : Player) extends GameObject(owner) {
  var ammo : Int
  def fire() = {
    this.ammo -= 1
  }
}