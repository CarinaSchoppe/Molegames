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
  private final BufferedReader keyboard;

  public ClientThread(Socket socket) throws IOException {
    super(socket);
    keyboard = new BufferedReader(new InputStreamReader(System.in));
    readFromKeyBoard();
  }

  private void readFromKeyBoard() {
    new Thread(() -> {
      System.out.println("Started Keyboard listener");
      try {
        while (true) {
          keyBoardInput = keyboard.readLine();
          System.out.println("clientInput:" + keyBoardInput);
          sendPacket(new Packet(keyBoardInput));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
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
