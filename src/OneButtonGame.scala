
/**
 * @author joonardo
 */

import scala.swing._
import event._
import java.awt.Toolkit

object OneButtonGame extends SimpleSwingApplication {
  val scr = new Screen()
  
  def top = new MainFrame {
    title = "OBG!!!"
    minimumSize = new Dimension(750, 400)
    contents = scr
    
    scr.requestFocus()
    this.ignoreRepaint = true
    this.pack()
    this.maximize()
    Timer(20){scr.repaint()}
  }
  Server.start()
  Timer(5){GameObjectRoot.update()}
}

object Timer {
  def apply(interval : Int, repeat : Boolean = true)(op : => Unit) = {
    val timeout = new javax.swing.AbstractAction(){
      def actionPerformed(e : java.awt.event.ActionEvent) = op
    }
    val timer = new javax.swing.Timer(interval, timeout)
    timer.setRepeats(repeat)
    timer.start()
  }
}