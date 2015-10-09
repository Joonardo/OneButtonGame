

/**
 * @author joonardo
 */

import java.util.Random
import scala.collection.mutable.Buffer
import math.round
import scala.swing.Graphics2D
import java.awt.Color

abstract class Powerup extends GameObject(null) {
  def collect(c : Character)
}

object Powerup {
  final val powerups = Buffer[Class[_]](
    new HealthPackage().getClass
  )
  def random() = {
    val v = round(Rng.getFloat()*(powerups.size - 1))
    powerups(v).newInstance().asInstanceOf[Powerup]
  }
}

class HealthPackage extends Powerup {
  var shouldMove = false
  var position = Rng.getPos()
  def collect(c : Character) = {
    c.health += 40
    this.destroy()
  }
  def paint(g : Graphics2D) = {
    val c = g.getColor
    g.setColor(Color.green)
    g.fillOval(this.x - 10, this.y - 10, 20, 20)
    g.setColor(c)
  }
  def update(dt : Double) = {
    GameObjectRoot.gameObjs.foreach { c =>
      if(c.isInstanceOf[Character] && (c.position - this.position).abs < c.radius + 10){
        this.collect(c.asInstanceOf[Character])
      }
    }
  }
}