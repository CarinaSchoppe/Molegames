package de.thundergames.network.client;

import de.thundergames.gameplay.player.PlayerHandler;
import de.thundergames.network.util.NetworkThread;

import java.io.IOException;
import java.net.Socket;

public class ClientThread extends NetworkThread {

  public ClientThread(Socket socket, int id) throws IOException {
    super(socket, id);
  }

  /**
   * @author Carina
   * @use disconnects the ClientThread from the System and will run the shutdown-logic of the playerSystem
   */
  @Override
  public void disconnect() {
    {
      try {
        System.out.println("Server disconnected!");
        socket.close();
        PlayerHandler.shutdown();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void setID(int id) {
    this.id = id;
  }
}
