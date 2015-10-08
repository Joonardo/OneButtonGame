

/**
 * @author joonardo
 */

//import java.awt.Graphics2D
import scala.swing._
import event._
import javax.swing.SwingUtilities

class Screen extends Panel {
  
  this.ignoreRepaint_=(true)
  
  listenTo(keys)
    
  reactions += {
    case KeyPressed(_, kc, _, _) => {
      if(GameObjectRoot.keys.contains(kc)){
        GameObjectRoot(kc).keyPressed()
      }else{
        GameObjectRoot.addPlayer(new Player(kc))
      }
    }
    case KeyReleased(_, kc, _, _) => {
      if(GameObjectRoot.keys.contains(kc)){
        GameObjectRoot(kc).keyReleased()
      }
    }
  }
  
  //this.self.setIgnoreRepaint(true)
  override def paintComponent(g : Graphics2D) = {
    super.paintComponent(g)
    g.clearRect(0, 0, this.size.width, this.size.height)
    //super.paint(g)
    for (go <- GameObjectRoot.gameObjs){
      go.paint(g)
    }
  }
  
  def getSize = (size.height, size.width)
  
  /*var frame : JFrame = null
  
  def init(){
    this.frame = new JFrame("OBG!!!")
    this.frame.setPreferredSize(Toolkit.getDefaultToolkit.getScreenSize)
    this.frame.setResizable(false)
    this.frame.setVisible(true)
    this.frame.setEnabled(true)
    this.frame.getContentPane().add(this)
    this.frame.addKeyListener(this)
    this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    this.frame.pack()
  }
  
  override def keyReleased(e : KeyEvent) = {
    var kc = e.getKeyCode
    if (!GameObjectRoot.getKeys.contains(kc)){
      var kh = new KeyHandler(kc)
      var p = new Player(kh)
      kh.parentWrapper = Some(p)
      this.frame.addKeyListener(kh)
      GameObjectRoot.addPlayer(p)
      println("Added " + kc)
    }
  }
  
  override def keyPressed(e : KeyEvent) = {
    
  }
  
  override def keyTyped(e : KeyEvent) = {}
  */
}