

/**
 * @author joonardo
 */

import math.{acos, sqrt, pow, cos, sin}

class Vector(val x : Double, val y : Double) {
  def +(v : Vector) = new Vector(this.x + v.x, this.y + v.y)
  def *(v : Vector) = v.x*this.x + v.y*this.y
  def *(d : Double) = new Vector(d*this.x, d*this.y)
  def -(v : Vector) = this + new Vector(-v.x, -v.y)
  def /(d : Double) = this*(1/d)
  def X(v : Vector) = this.x*v.y - v.x*this.y
  def abs = sqrt(pow(this.x, 2) + pow(this.y, 2))
  def unit = this/this.abs
  override def toString = this.x + "*i + " + this.y + "*j"
}

object Vector {
  def normal(x : Double, y : Double) = new Vector(x, y)
  def polar(a : Double, abs : Double) = new Vector(cos(a), sin(a))*abs
  def angleBetween(v1 : Vector, v2 : Vector) = acos((v1.abs*v2.abs)/(v1*v2))
}