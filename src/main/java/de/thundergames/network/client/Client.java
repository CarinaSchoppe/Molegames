package de.thundergames.network.client;

import de.thundergames.MoleGames;
import de.thundergames.network.util.Network;
import de.thundergames.network.util.Packet;
import de.thundergames.network.util.Packets;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Client extends Network {
  private static final boolean keyListener = true;
  private static Client client;
  private final String name;
  private final ClientPacketHandler clientPacketHandler;
  private final ArrayList<Integer> moleIDs = new ArrayList<>();
  /**
   * @param port of the server to connect to if empty its 291220
   * @param ip of the server to connect to if empty its "localhost"
   */
  private ClientThread clientThread;

  private int id;
  private int gameID;

  public Client(final int port, @NotNull final String ip, @NotNull final String name) {
    super(port, ip);
    this.name = name;
    clientPacketHandler = new ClientPacketHandler();
  }

  public static boolean isKeyListener() {
    return keyListener;
  }

  /**
   * @author Carina
   * @use creates the main Thread for the Client logic
   * @see MoleGames
   */
  public static void ClientMain() {
    client = new Client(5000, "127.0.0.1", "Carina");
    client.create();
  }

  public static Client getClient() {
    return client;
  }

  /**
   * @author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we
   *     create the Constructor and call the create object to create the sockets and stream
   * @see Client
   */
  @Override
  public void create() {
    connect();
  }

  /**
   * @author Carina
   * @use connects the client to the server
   * @see ClientThread for more
   */
  public void connect() {
    try {
      socket = new Socket(ip, port);
      clientThread = new ClientThread(socket, 0, this);
      clientThread.start();
      test();
    } catch (IOException | InterruptedException e) {
      System.out.println("Is the server running?!");
    }
  }

  /** @author Carina Logic to test some things! */
  public void test() throws InterruptedException {
    JSONObject object;
    object = new JSONObject();
    object.put("name", name);
    object.put("type", Packets.NAME.getPacketType());
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.CREATEGAME.getPacketType());
    var packet = new Packet(object);
    clientThread.sendPacket(packet);
    Thread.sleep(100);
    var jsonObject = new JSONObject();
    jsonObject.put("type", Packets.JOINGAME.getPacketType());
    jsonObject.put("connectType", "player");
    jsonObject.put("gameID", 0);
    object = new JSONObject();
    object.put("type", Packets.CONFIGURATION.getPacketType());
    object.put("gameID", 0);
    object.put("punishment", 0);
    object.put("floors", 5);
    object.put("radius", 2);
    clientThread.sendPacket(new Packet(object));
    clientThread.sendPacket(new Packet(jsonObject));
    object = new JSONObject();
    object.put("type", Packets.GAMESTART.getPacketType());
    object.put("gameID", 0);
    clientThread.sendPacket(new Packet(object));
    Thread.sleep(1000);
    object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    object.put("x", 3);
    object.put("y", 2);
    object.put("moleID", 1);
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    object.put("x", 0);
    object.put("y", 0);
    object.put("moleID", 2);
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    object.put("moleID", 0);
    object.put("x", 2);
    object.put("y", 1);
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    object.put("x", 2);
    object.put("y", 4);
    object.put("moleID", 0);
    clientThread.sendPacket(new Packet(object));
  }

  public ClientPacketHandler getClientPacketHandler() {
    return clientPacketHandler;
  }

  public void setId(final int id) {
    this.id = id;
  }

  public ClientThread getClientThread() {
    return clientThread;
  }

  public ArrayList<Integer> getMoleIDs() {
    return moleIDs;
  }

  public void setGameID(final int gameID) {
    this.gameID = gameID;
  }
}
