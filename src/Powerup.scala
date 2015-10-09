

/**
 * @author joonardo
 */

import java.util.Random
import scala.collection.mutable.{Buffer, Map}
import java.io.File
import math.round
import scala.swing.Graphics2D
import java.awt.{Color, Image}
import java.awt.geom.Ellipse2D
import javax.imageio.ImageIO

abstract class Powerup extends GameObject(null) with Hittable {
  val pic : Image
  var position = Rng.getPos()
  var shouldMove = false
  var health = 30
  override def radius = 12
  def collect(c : Character)
  def update(dt : Double) = {
    GameObjectRoot.gameObjs.foreach { c =>
      if(c.isInstanceOf[Character] && (c.position - this.position).abs < c.radius + this.radius){
        this.collect(c.asInstanceOf[Character])
      }
    }
  }
  def paint(g : Graphics2D) = {
    g.drawImage(this.pic, this.x-10, this.y-10, null)
  }
}

object Powerup {
  final val powerups = Buffer[Class[_]](
    classOf[HealthPackage],
    classOf[WeaponPackage]
  )
  def random() = {
    val v = round(Rng.getFloat()*(powerups.size - 1))
    powerups(v).newInstance().asInstanceOf[Powerup]
  }
}

class HealthPackage extends Powerup {
  val pic = ImageIO.read(new File("src/Media/HealthPackage.png"))
  def collect(c : Character) = {
    c.health += 40
    this.destroy()
  }
}

class WeaponPackage extends Powerup {
  val pic = ImageIO.read(new File("src/Media/WeaponPackage.png"))
  val contents = classOf[Shotgun]
  def collect(c : Character) = {
    c.weapon = this.contents.getConstructor(classOf[Player]).newInstance(c.owner)
    this.destroy()
  }
}

/*object Media {
  val pics = Map[String, Image]()
  def load() = {
    for(i <- 0 until Powerup.powerups.size){
      println("Name: " + Powerup.powerups(i).getCanonicalName)
      //pics += p(i).getName -> ImageIO.read(new File("src/Media/" + p(i).getName + ".png")).getScaledInstance(20, 20, 0)
    }
  }
  
  def apply(s : String) = pics(s)
}*/