

/**
 * @author joonardo
 */

import java.awt.{Color, Graphics2D}
import java.util.Random
import math.{Pi, round, cos, sin, pow, abs, signum}

abstract class GameObject(val owner : Player) {
  var position : Vector
  var shouldBeRemoved = false
  var shouldMove : Boolean
  var lastUpdate = System.currentTimeMillis()
  def diameter = 0.0
  var velocity = Vector.normal(0, 0)
  def radius = toInt(diameter/2)
  
  def paint(g : Graphics2D) : Unit
  def update(dt : Double) : Unit
  
  def destroy() : Unit = this.shouldBeRemoved = true
  
  def x = toInt(this.position.x)
  def y = toInt(this.position.y)
  def toInt(d : Double) = round(d).asInstanceOf[Int]
  
  def checkCollision() = {
    if(this.x - this.radius < 0 || this.x + this.radius > GameObjectRoot.areaWidth){
      this.velocity = Vector.normal(-this.velocity.x, this.velocity.y)
      if(this.position.x - GameObjectRoot.areaWidth/2 < 0){
        this.position = Vector.normal(1 + this.radius, this.position.y)
      }else{
        this.position = Vector.normal(GameObjectRoot.areaWidth - (1 + this.radius), this.position.y)
      }
    }
    if(this.y -this.radius < 0 || this.y + this.radius > GameObjectRoot.areaHeight){
      this.velocity = Vector.normal(this.velocity.x, -this.velocity.y)
      if(this.position.y - GameObjectRoot.areaHeight/2 < 0){
        this.position = Vector.normal(this.position.x, 1 + this.radius)
      }else{
        this.position = Vector.normal(this.position.x, GameObjectRoot.areaHeight - (1 + this.radius))
      }
    }
  }
}



class Character(owner : Player) extends GameObject(owner) {
  var shouldMove = false
  var health = 100
  val maxHealth = 200
  var position = Rng.getPos()
  val maxVelocity = 300.0
  var angVel = 0.0
  val maxAngVel = 2.0
  var left = true
  var dir = Pi/6
  val spawned = System.currentTimeMillis()
  val spawnProtection = 7000L
  override def diameter = this.health/4 + 20
  def lifeTime = System.currentTimeMillis() - this.spawned
  private val shootingThreshold = 200L //ms
  
  override def destroy() = {
    super.destroy()
    this.owner.alive = false
    this.owner.died = System.currentTimeMillis()
  }
    
  def takeHit(dmg : Int) = if(this.lifeTime > this.spawnProtection) this.health -= dmg
  
  def shoot() = {
    GameObjectRoot.addGameObject(new Bullet(this.owner, this.dir, this.position + Vector.polar(dir, this.radius*1.4)))
  }
  
  def paint(g : Graphics2D) = {
    //Draw outer circle
    g.drawOval(this.x - this.radius, this.y - this.radius, toInt(this.diameter), toInt(this.diameter))
    //Draw inner circle
    val lt = this.lifeTime
    if(lt > this.spawnProtection || lt / 200 % 2 == 1){
      g.drawOval(this.x - 10, this.y - 10, 20, 20)
    }
    //Draw name
    g.drawString(this.owner.key + "", this.x - this.radius - 3, this.y - this.radius - 3)
    //Draw gun
    var rMax = this.radius + 10
    var rMin = this.radius - 10
    g.drawLine(toInt(this.x + rMin*cos(this.dir)), toInt(this.y + rMin*sin(this.dir)), toInt(this.x + rMax*cos(this.dir)), toInt(this.y + rMax*sin(this.dir)))
    //Draw dir indicator
    var ang = dir + (if (left) - 0.3 else  0.3)
    var c = g.getColor
    g.setColor(Color.RED)
    g.fillOval(this.x + toInt((rMax - 6)*cos(ang)) - 2, this.y + toInt((rMax-6)*sin(ang)) - 2, 4, 4)
    g.setColor(c)
  }
  
  def update(dt : Double) = {
    if(this.shootingThreshold > this.owner.pressedTime.getOrElse(1000L)){
      this.shoot()
    }
    
    //Rotate
    
    if(this.shouldMove) {
      this.angVel = (if(left) -1 else 1)*maxAngVel
    }else if (abs(this.angVel) > 0){
      this.angVel += (if(this.angVel < 0) 1 else -1) * 6 * dt
      if(abs(this.angVel) < 0.1){
        this.angVel = 0
      }
    }
    this.dir += angVel*dt
    
    //Move
    
    if(this.shouldMove){
      this.velocity += Vector.polar(dir, 400*dt)
    }
    this.velocity *= pow(.25, dt)
    this.position = this.position + this.velocity*dt
    
    this.dir = this.dir % (2*Pi)
    checkCollision()
    if(this.health > this.maxHealth){
      this.health = this.maxHealth
    }
  }
}

/*class Bullet(owner : Player, val dir : Double, var position : Vector) extends GameObject(owner) {
  val lifetime = 2500
  private val born = System.currentTimeMillis()
  private val damage = 10
  var timeAlive = 0
  var shouldMove = true
  velocity = Vector.polar(dir, 350) + this.owner.character.velocity
  
  val recoil = Vector.polar(dir, 45)
  
  this.owner.character.velocity -= recoil
  
  override def paint(g : Graphics2D) = {
    g.drawLine(this.x, this.y, toInt(this.x-10*this.velocity.unit.x), toInt(this.y-10*this.velocity.unit.y))
    //g.drawLine(this.x, this.y, toInt(this.x-10*cos(this.dir)), toInt(this.y-10*sin(this.dir)))
  }
  
  override def update(dt : Double) = {
    if(System.currentTimeMillis() - this.born > this.lifetime){
      this.destroy()
    }else{
      this.position = this.position + this.velocity*dt
      GameObjectRoot.gameObjs.foreach { x => {
        if(x.getClass == classOf[Character]){
          val c = x.asInstanceOf[Character]
          if((this.position - c.position).abs <= c.radius){
            c.takeHit(this.damage)
            c.velocity += recoil
            if(c.health <= 0){
              c.destroy()
            }
            this.destroy()
          }
        }
      }}
    }
    checkCollision()
  }
}*/