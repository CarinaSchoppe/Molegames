package network.client;

import network.util.Network;
import network.util.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client extends Network {
  /**
   *
   * @param port of the server to connect to if empty its 291220
   * @param ip of the server to connect to if empty its "localhost"
   */
  public Client(int port, String ip) {
    super(port, ip);
    try {
      socket = new Socket(ip, port);
      ClientThread clientThread = new ClientThread(socket);
      clientThread.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @author Carina
   * @param packet that got send to a client
   */
  public static void handlePacket(Packet packet) {
    //TODO: Client logik
  }
}
