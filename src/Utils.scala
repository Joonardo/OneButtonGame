import math.round
import scala.swing._

object Utils {
  def toInt(a : Double) = round(a).asInstanceOf[Int]
  val FONT_WIDTH = 6
  val FONT_HEIGHT = 7
  val KickThreshold = 15000L
}