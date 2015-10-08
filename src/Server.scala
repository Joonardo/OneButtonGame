

/**
 * @author joonardo
 */

import scala.swing.Publisher
import scala.swing.event.Event
import java.net.{ServerSocket, Socket}
import java.io._

object Server extends Thread("Server") with Publisher {
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
  if(socket.getInetAddress.isAnyLocalAddress()) println(socket.getInetAddress.getAddress)
  val uid = "qwerty"
  override def run() = {
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
}

case class SocketKeyUp(val uid : String) extends Event
case class SocketKeyDown(val uid : String) extends Event