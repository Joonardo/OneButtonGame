
import math.Pi
import scala.swing.Graphics2D
import java.awt.Color

class Caliper45(owner : Player, dir : Option[Double] = None) extends Bullet(owner) { //For shotgun
  val _damage = 15
  val _lifetime = 2500L
  val _velocity = 700.0
  val _recoil = 10
  velocity = Vector.polar(dir.getOrElse(this.char.dir), _velocity) + this.owner.character.velocity
  this.owner.character.velocity -= this.recoil
}

class Slug(owner : Player) extends Bullet(owner) { //For shotgun
  val _damage = 10
  val _lifetime = 800L
  val _velocity = 600*(Rng.getFloat() + 0.5)
  val _recoil = 10
  velocity = Vector.polar(this.char.dir, _velocity) + this.char.velocity
  
  def applyRecoil() = this.char.velocity -= this.recoil
}

class AK47Bullet(owner : Player) extends Bullet(owner) { //For shotgun
  val _damage = 10
  val _lifetime = 1500L
  val _velocity = 900.0
  val _recoil = 20
  velocity = Vector.polar(this.char.dir, _velocity) + this.char.velocity
  this.char.velocity -= this.recoil
}

class Mine(owner : Player) extends GameObject(Some(owner)) with Hittable {
  GameObjectRoot.addGameObject(this)
  
  var health = 60
  var position = Vector(this.owner.character.position)
  var shouldMove = false
  
  private val born = System.currentTimeMillis()
  private val chargeTime = 3000L
  private var charged = false
  override val radius = 20
  
  velocity = Vector.normal(0, 0)
  
  override def destroy(destroyer : Player) = {
    if(this.charged){
      for(i <- 1 to 100) {
        new Shard(this.position, Pi*i/50, this.owner)
      }
      super.destroy(destroyer)
    }
  }
  
  def update(dt : Double) = {
    if(!charged && System.currentTimeMillis() - this.born > this.chargeTime){
      charged = true
    }else if(charged){
      GameObjectRoot.gameObjs.foreach { obj => 
        if(obj.getClass == classOf[Character]){
          val char = obj.asInstanceOf[Character]
          if((char.position - this.position).abs < this.radius + char.radius){
            this.destroy(char.owner)
          }
        }
      }
    }
  }
  
  def paint(g : Graphics2D) = {
    val c = g.getColor
    g.setColor(if(this.charged) Color.BLACK else Color.GRAY)
    /*if(this.charged)
      g.fillOval(this.x - this.radius, this.y -this.radius, 2*this.radius, 2*this.radius)
    else*/
    g.drawOval(this.x - this.radius, this.y -this.radius, 2*this.radius, 2*this.radius)
  }
}

class Shard(pos : Vector, dir : Double, owner : Player) extends Bullet(owner){
  val _damage = 5
  val _velocity = (0.7 + Rng.getFloat()/2)*500
  velocity = Vector.polar(dir, _velocity)
  position = pos
  val _recoil = 10
  val _lifetime = 1000L
  def fire() = {}
}