
class Caliper45(owner : Player, dir : Double, pos : Vector) extends Bullet(owner, dir, pos) { //For shotgun
  val _damage = 15
  val _lifetime = 2500L
  val _velocity = 700.0
  val _recoil = 10
  velocity = Vector.polar(dir, _velocity) + this.owner.character.velocity
  this.owner.character.velocity -= this.recoil
}

class Slug(owner : Player, dir : Double, pos : Vector) extends Bullet(owner, dir, pos) { //For shotgun
  val _damage = 10
  val _lifetime = 800L
  val _velocity = 600*(Rng.getFloat() + 0.5)
  val _recoil = 10
  velocity = Vector.polar(dir, _velocity) + this.owner.character.velocity
  
  def applyRecoil() = this.owner.character.velocity -= this.recoil
}

class AK47Bullet(owner : Player, dir : Double, pos : Vector) extends Bullet(owner, dir, pos) { //For shotgun
  val _damage = 10
  val _lifetime = 1500L
  val _velocity = 900.0
  val _recoil = 20
  velocity = Vector.polar(dir, _velocity) + this.owner.character.velocity
  this.owner.character.velocity -= this.recoil
}