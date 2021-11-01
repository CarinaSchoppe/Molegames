package network.client;

import gameplay.player.PlayerHandler;
import network.util.NetworkThread;
import network.util.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends NetworkThread {
  private String keyBoardInput;

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
        System.out.println("Server disconnected!");
        socket.close();
        PlayerHandler.shutdown();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
