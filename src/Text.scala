

/**
 * @author joonardo
 */

import java.awt.{Image, Rectangle, Color}
import java.util.Hashtable
import java.io.File
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.collection.mutable.{Buffer, Map}
import scala.swing.Graphics2D
import Utils._

class Text(var letters : Buffer[Image], val parent : Option[GameObject], scale : Double, relativePosition : Vector) extends GameObject(null) {
  var shouldMove = false
  val fontWidth = toInt(6*scale)
  var position = relativePosition
  
  override def x = if(this.parent.isDefined) this.parent.get.x + toInt(this.position.x) else GameObjectRoot.areaWidth/2
  override def y = if(this.parent.isDefined) this.parent.get.y + toInt(this.position.y) else GameObjectRoot.areaHeight/2
  
  def update(dt : Double) = {
    
  }
  
  def paint(g : Graphics2D) = {
    for(i <- 0 until letters.size){
      g.drawImage(letters(i), this.x + this.fontWidth*i, this.y, null)
    }
  }
}

object Text {
  private val chars = Map[Char, BufferedImage]()
  
  private def init() = {
    val basePhoto = ImageIO.read(new File("src/Media/Fonts.png"))
    val chars = "abcdefghijklmnopqrstuvwxyz1234567890/*-+.,:"
    for(i <- 0 until chars.length){
      var tmp = basePhoto.getSubimage(i*FONT_WIDTH, 0, FONT_WIDTH, FONT_HEIGHT)
      this.chars += chars(i) -> tmp
    }
  }
  
  init()
  
  private def getScaled(img : BufferedImage, scale : Double) = {
    img.getScaledInstance(
        toInt(scale*img.getWidth), 
        toInt(scale*img.getHeight), 
        Image.SCALE_FAST)
  }
  
  private def newImage(b : BufferedImage) = {
    val w = b.getWidth
    val h = b.getHeight
    val newImg = new BufferedImage(w, h, b.getType)
    for(i <- 0 until w){
      for(j <- 0 until h){
        newImg.setRGB(i, j, b.getRGB(i, j))
      }
    }
    newImg
  }
  
  def apply(s : String, scale : Double = 1, parent : Option[GameObject] = None, pos : Vector = new Vector(0,0), alpha : Double = 1) = {
    val letters = Buffer[Image]()
    for (c <- s.toLowerCase()){
      if(chars.contains(c)){
        var tmp = newImage(this.chars(c))
        setAlpha(tmp, alpha)
        letters += getScaled(tmp, scale)
      }
    }
    new Text(letters, parent, scale, pos)
  }
  
  private def setAlpha(img : BufferedImage, a : Double) = {
    for(i <- 0 until img.getWidth){
      for(j <- 0 until img.getHeight){
        val col = img.getRGB(i, j)
        val alpha = (col >> 24) & 0xff
        val red = (col >> 16) & 0xff
        val green = (col >> 8) & 0xff
        val blue = col & 0xff
        val rgb = new Color(red, green, blue, toInt(alpha*a))
        img.setRGB(i, j, rgb.getRGB)
      }
    }
  }
}