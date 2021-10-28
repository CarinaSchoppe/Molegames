package network.client;

import gameplay.player.PlayerHandler;
import network.util.NetworkThread;

import java.io.IOException;
import java.net.Socket;

public class ClientThread extends NetworkThread {

  public ClientThread(Socket socket) throws IOException {
    super(socket);
  }

  /**
   * @author Carina
   * @use disconnects the ClientThread from the System and will run the shutdown-logic of the playerSystem
   */
  @Override
  public void disconnect() {
    {
      try {
        socket.close();
        PlayerHandler.shutdown();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
