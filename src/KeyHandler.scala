

/**
 * @author joonardo
 */

import java.awt.event._

class KeyHandler(val key : Int) extends KeyListener {
  
  var parentWrapper : Option[Player] = None
  
  private def parent = this.parentWrapper.get
  
  override def keyReleased(e : KeyEvent) = {
     if(e.getKeyCode == this.key){
       this.parent.character.shouldMove = false
     }
  }
  
  override def keyPressed(e : KeyEvent) = {
    if(e.getKeyCode == this.key && !this.parent.character.shouldMove){
       this.parent.character.shouldMove = true
       this.parent.character.left = !this.parent.character.left
     }
  }
  
  override def keyTyped(e : KeyEvent) = {}
}