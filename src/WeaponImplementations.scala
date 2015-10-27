

/**
 * @author joonardo
 */
//Default weapon

import scala.collection.mutable.Buffer

class Colt45(owner : Player) extends Weapon(owner) {
  def fire() = {
    val c = this.holder
    val p = this.owner
    val b = new Caliper45(p, c.dir, c.position + Vector.polar(c.dir, c.radius*1.4))
  }
}

class Shotgun(owner : Player) extends Weapon(owner) {
  val scattering = math.Pi/6.0
  def fire() = {
    this.ammo -= 1
    if(this.ammo <= 0){
      this.holder.weapon = new Colt45(this.owner)
    }
    
    val slugs = Buffer[Slug]()
    for (_ <- 0 until 15){
      val newDir = this.holder.dir + this.scattering*(Rng.getFloat() - 1)
      val c = this.holder
      val p = this.owner
      val b = new Slug(p, newDir, c.position + Vector.polar(c.dir, c.radius*1.4))
      slugs += b
    }
    for(b <- slugs){
      b.applyRecoil()
    }
  }
}

class AK47(owner : Player) extends Weapon(owner) {
  var shouldShoot = false
  val shootingInterval = 50L
  private var lastShot = System.currentTimeMillis()
  override def checkTrigger() = {
    if(this.owner.pressedTime.getOrElse(1000L) < this.shootingThreshold){
      this.shouldShoot = !this.shouldShoot
    }
    lastShot + this.shootingInterval < System.currentTimeMillis() && this.shouldShoot
  }
  def fire() = {
    val c = this.holder
    val p = this.owner
    val b = new AK47Bullet(p, c.dir, c.position + Vector.polar(c.dir, c.radius*1.4))
    
    this.ammo -= 1
    if(this.ammo == 0){
      this.holder.weapon = new Colt45(this.owner)
    }
    this.lastShot = System.currentTimeMillis()
  }
}

abstract class MissileLauncher(owner : Player) extends Weapon(owner){
}