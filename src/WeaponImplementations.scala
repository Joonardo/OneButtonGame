

/**
 * @author joonardo
 */
//Default weapon

import scala.collection.mutable.Buffer

class Colt45(owner : Player) extends Weapon(owner) {
  var ammo = -1
  def fire() = {
    val c = this.holder
    val p = this.owner
    val b = new Bullet(p, c.dir, c.position + Vector.polar(c.dir, c.radius*1.4))
    GameObjectRoot.addGameObject(b)
    b.applyRecoil()
  }
}

class Shotgun(owner : Player) extends Weapon(owner) {
  var ammo = 15
  val scattering = math.Pi/6.0
  def fire() = {
    this.ammo -= 1
    if(this.ammo <= 0){
      this.holder.weapon = new Colt45(this.owner)
    }
    
    val slugs = Buffer[Bullet]()
    for (_ <- 0 until 15){
      val newDir = this.holder.dir + this.scattering*(Rng.getFloat() - 1)
      val c = this.holder
      val p = this.owner
      val b = new Bullet(p, newDir, c.position + Vector.polar(c.dir, c.radius*1.4))
      b.setWeaponAttr(500, 10, 300.0*(Rng.getFloat() + 0.5), 10)
      slugs += b
    }
    for(b <- slugs){
      GameObjectRoot.addGameObject(b)
      b.applyRecoil()
    }
  }
}

abstract class MissileLauncher(owner : Player) extends Weapon(owner){
  ammo = 3
}