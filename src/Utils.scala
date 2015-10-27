
import math.round
import scala.xml.XML
import scala.collection.mutable.Buffer

object Utils {
  def toInt(a : Double) = round(a).asInstanceOf[Int]
  val FONT_WIDTH = 6
  val FONT_HEIGHT = 7
}

object Settings {
  private var xml = XML.loadFile("src/settings.xml")
  //private var playerPath = xml \\ "settings" \\ "player"
  
  def reload() = xml = XML.loadFile("src/settings.xml")
  
  def Bounty = ((xml \\ "settings" \\ "score" \\ "@bounty") text).toInt
  def SuicidePenalty = ((xml \\ "settings" \\ "score" \\ "@suicidePenalty") text).toInt
  def KickThreshold = ((xml \\ "settings" \\ "player" \ "@KickThreshold") text).toLong * 1000
  def RespawnTime = ((xml \\ "settings" \\ "player" \ "@RespawnTime") text).toLong * 1000
  def SpawnProtection = ((xml \\ "settings" \\ "character" \ "@SpawnProtection") text).toLong * 1000
  def Ammo(w : Class[_]) = ((xml \\ "settings" \\ "weapon" \\ w.getCanonicalName \ "@ammo") text).toInt
  def InitialHealth = ((xml \\ "settings" \\ "character" \\ "@initialHealth") text).toInt
  def MaxHealth = ((xml \\ "settings" \\ "character" \\ "@maxHealth") text).toInt
  def Acceleration = ((xml \\ "settings" \\ "character" \\ "@acceleration") text).toInt
  def Friction = ((xml \\ "settings" \\ "character" \\ "@friction") text).toDouble
  def PowerupMinTime = ((xml \\ "settings" \\ "powerup" \\ "@minTime") text).toInt * 1000
  def PowerupInterval = ((xml \\ "settings" \\ "powerup" \\ "@interval") text).toInt * 1000
  def PowerupAttr(c : Class[_]) = xml \\ "settings" \\ "powerup" \\ c.getCanonicalName
  def EnabledWeapons = {
    val items = PowerupAttr(classOf[WeaponPackage])
    val enabled = items.flatMap { x => x.attributes }.map{x => if(x.value.text.toBoolean) Some(x.key) else None}
    val res = Buffer[Class[_]]()
    enabled.foreach { x => if(x.isDefined) res += Class.forName(x.get.asInstanceOf[String]) }
    res
  }
}