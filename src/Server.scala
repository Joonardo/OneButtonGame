

/**
 * @author joonardo
 */

import scala.swing.Publisher
import scala.swing.event.Event
import java.net.{ServerSocket, Socket}
import java.io._

object Server extends Thread("Server") {
  override def run() = {
    val listener = new ServerSocket(55000)
    while(true){
      val newClient = new ServerThread(listener.accept())
      newClient.start()
    }
  }
}

class ServerThread(val socket : Socket) extends Thread with Publisher {
  val in = new DataInputStream(socket.getInputStream)
  OneButtonGame.scr.listenTo(this)
  var uid = ""
  
  override def run() = {
    println("New client: " + this.socket.getInetAddress.getHostAddress)
    this.do_handshake()
    while(socket.isConnected()){
      this.recv() match {
        case "UP" => this.publish(new SocketKeyUp(uid))
        case "DOWN" => this.publish(new SocketKeyDown(uid))
        case _ =>
      }
    }
    socket.close()
    OneButtonGame.scr.deafTo(this)
  }
  
  def recv() = {
    var data = ""
    do {
      data += in.readByte().toChar
    } while(!data.endsWith("\r\n"))
    data.stripLineEnd
  }
  
  def do_handshake() = {
    var tmp = ""
    do {
      tmp = this.recv()
    }while(GameObjectRoot.keys.contains(tmp))
    this.uid = tmp
  }
}

case class SocketKeyUp(val uid : String) extends Event
case class SocketKeyDown(val uid : String) extends Event