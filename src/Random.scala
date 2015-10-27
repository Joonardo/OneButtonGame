

/**
 * @author joonardo
 */

import java.util.Random
import Settings._

object Rng {
  val rng = new Random()
  
  def getPos() = Vector.normal(50 + rng.nextFloat() * (GameObjectRoot.areaWidth - 100), 50 + rng.nextFloat() * (GameObjectRoot.areaHeight - 100))
  //def getInt(from : Int, to : Int) : Int = from + math.round((to - from)*rng.nextFloat())
  def getFloat() = rng.nextFloat()
  def nextPowerupSpawnTime = System.currentTimeMillis() + PowerupMinTime + rng.nextInt(PowerupInterval)
}