

/**
 * @author joonardo
 */
//Default weapon

import scala.collection.mutable.Buffer

class Colt45(owner : Player) extends Weapon(owner) {
  def fire() = {
    val p = this.owner
    new Caliper45(p)
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
    for (_ <- 0 until 25){
      val newDir = this.holder.dir + this.scattering*(Rng.getFloat() - 1)
      val p = this.owner
      val b = new Slug(p, newDir)
      slugs += b
    }
    for(b <- slugs){
      b.applyRecoil()
    }
  }
}

class AK47(owner : Player) extends Weapon(owner) {
  var shouldShoot = false
  val shootingInterval = 20L
  private var lastShot = System.currentTimeMillis()
  override def checkTrigger() = {
    if(this.owner.pressedTime.getOrElse(1000L) < this.shootingThreshold){
      this.shouldShoot = !this.shouldShoot
    }
    lastShot + this.shootingInterval < System.currentTimeMillis() && this.shouldShoot
  }
  def fire() = {
    val p = this.owner
    new AK47Bullet(p)
    
    this.ammo -= 1
    if(this.ammo == 0){
      this.holder.weapon = new Colt45(this.owner)
    }
    this.lastShot = System.currentTimeMillis()
  }
}

class Mines(owner : Player) extends Weapon(owner){
  def fire() = {
    new Mine(this.owner)
    
    this.ammo -= 1
    if(this.ammo == 0){
      this.holder.weapon = new Colt45(this.owner)
    }
  }
}

abstract class MissileLauncher(owner : Player) extends Weapon(owner){
}