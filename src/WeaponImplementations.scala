

/**
 * @author joonardo
 */

//Default weapon
abstract class Colt45(owner : Player) extends Weapon(owner) {
  ammo = -1 //Infinite ammo
}

abstract class Shotgun(owner : Player) extends Weapon(owner) {
  ammo = 5
}

abstract class MissileLauncher(owner : Player) extends Weapon(owner){
  ammo = 3
}