package network.server;

import network.util.NetworkThread;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends NetworkThread {

  private final int id;

  /**
   * @author Carina
   * @param socket the server Socket
   * @param id Serverthread id
   */
  public ServerThread(Socket socket, int id) {
    super(socket);
    this.id = id;
  }

  public int getThreadId() {
    return id;
  }

  /**
   * @author Carina
   * @use disconnects the serverThread and removes it from the lists and maps
   */
  @Override
  public void disconnect() {
    {
      try {
        Server.getClientThreads().remove(this);
        Server.getThreadIds().remove(this.getThreadId());
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


}
