

/**
 * @author joonardo
 */

import scala.swing.Publisher
import scala.swing.event.Event
import java.net.{ServerSocket, Socket}
import java.io._

object Server extends Thread with Publisher {
  reactions += {
    case SocketKeyDown(uid) => {this.publish(new SocketKeyDown(uid))}
    case SocketKeyUp(uid) => {this.publish(new SocketKeyUp(uid))}
  }
  override def run() = {
    val listener = new ServerSocket(55000)
    while(true){
      val newClient = new ServerThread(listener.accept())
      this.listenTo(newClient)
      newClient.start()
    }
  }
}

class ServerThread(val socket : Socket) extends Thread with Publisher {
  override def run() = {
    while(true){
      
    }
  }
}

case class SocketKeyUp(val uid : String) extends Event
case class SocketKeyDown(val uid : String) extends Event