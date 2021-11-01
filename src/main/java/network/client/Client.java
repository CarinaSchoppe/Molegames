package network.client;

import blitzgames.MoleGames;
import network.util.Network;
import network.util.Packet;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Client extends Network {
  /**
   * @param port of the server to connect to if empty its 291220
   * @param ip   of the server to connect to if empty its "localhost"
   */

  private ClientThread clientThread;
  private int id;
  private int gameID;
  private static Client client;
  private final ClientPacketHandler clientPacketHandler;
  private static final boolean keyListener = true;

  public static boolean isKeyListener() {
    return keyListener;
  }

  /**
   * @param args
   * @author Carina
   * @use creates the main Thread for the Client logic
   * @see MoleGames
   */
  public static void main(String... args) {
    client = new Client(5000, "127.0.0.1");
    client.create();
  }

  public Client(int port, String ip) {
    super(port, ip);
    clientPacketHandler = new ClientPacketHandler();
  }

  /**
   * @throws IOException
   * @author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we create the Constructor and call the create object to create the sockets and stream
   * @see Client
   */
  @Override
  protected void create() {
    try {
      socket = new Socket(ip, port);
      clientThread = new ClientThread(socket, 0);
      clientThread.start();
      test();
    } catch (ConnectException exe) {
      System.out.println("Cant connect to server? Is it running?");
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * @author Carina
   * Logic to test some things!
   */
  public void test() throws InterruptedException {
    clientThread.sendPacket(new Packet("CREATE-GAME#0"));
    Thread.sleep(1000);
    clientThread.sendPacket(new Packet("JOIN-GAME#0"));
  }

  public ClientPacketHandler getClientPacketHandler() {
    return clientPacketHandler;
  }

  public static Client getClient() {
    return client;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ClientThread getClientThread() {
    return clientThread;
  }

  public void setGameID(int gameID) {
    this.gameID = gameID;
  }
}
