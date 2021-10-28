package network.client;

import network.util.Network;
import network.util.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client extends Network {
  /**
   * @param port of the server to connect to if empty its 291220
   * @param ip   of the server to connect to if empty its "localhost"
   */

  /**
   * @author Carina
   * @use creates the main Thread for the Client logic
   * @see main.MoleGames
   * @param args
   */
  public static void main(String... args) {
    Client client = new Client(5000, "127.0.0.1");
    client.create();
  }

  public Client(int port, String ip) {
    super(port, ip);
  }

  /**
   * @param packet that got send to a client
   * @author Carina
   */
  public static void handlePacket(Packet packet) {
    //TODO: Client logik
  }

  /**@author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we create the Constructor and call the create object to create the sockets and stream
   * @see Client
   * @throws IOException
   */
  @Override
  protected void create() {
    try {
      socket = new Socket(ip, port);
      ClientThread clientThread = new ClientThread(socket);
      clientThread.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
