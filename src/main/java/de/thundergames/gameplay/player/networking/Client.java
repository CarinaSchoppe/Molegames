/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 15:51 by Carina
 * Latest changes made by Carina on 15.11.21, 15:10
 * All contents of "Client" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import de.thundergames.networking.util.Network;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Client extends Network {

  private static final boolean keyListener = true;
  protected static Client client;
  private final ArrayList<Integer> moleIDs = new ArrayList<>();
  protected ClientPacketHandler clientPacketHandler;
  protected ClientThread clientThread;
  private String name;
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

  public static Client getClient() {
    return client;
  }


  public static void main(String[] args) throws InterruptedException {

    Client client = new Client(5000, "127.0.0.1", "Spieler");
    client.create();
    client.test();
  }

  /**
   * @author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we create the Constructor and call the create object to create the sockets and stream
   * @see Client
   */
  @Override
  public void create() {
    connect();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    } catch (IOException exception) {
      System.out.println("Is the server running?!");
    }
  }

  /**
   * @author Carina Logic to test some things!
   */
  public void test() throws InterruptedException {
    JSONObject object;
    object = new JSONObject();
    var json = new JSONObject();
    json.put("name", name);
    object.put("values", json.toString());
    object.put("type", Packets.NAME.getPacketType());
    System.out.println("namenpacke " + object);
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.CREATEGAME.getPacketType());
    json.put("gameID", 0);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));

    var jsonObject = new JSONObject();
    jsonObject.put("type", Packets.JOINGAME.getPacketType());
    json = new JSONObject();
    json.put("connectType", "player");
    json.put("gameID", 0);
    jsonObject.put("values", json.toString());
    object = new JSONObject();
    object.put("type", Packets.CONFIGURATION.getPacketType());
    json = new JSONObject();
    json.put("gameID", 0);
    json.put("punishment", 0);
    json.put("floors", 5);
    json.put("radius", 2);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));
    clientThread.sendPacket(new Packet(jsonObject));
    object = new JSONObject();
    object.put("type", Packets.GAMESTART.getPacketType());
    json = new JSONObject();
    json.put("gameID", 0);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));
    Thread.sleep(1000);
    object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    json = new JSONObject();
    json.put("x", 3);
    json.put("y", 2);
    json.put("moleID", 1);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    json = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    json.put("x", 0);
    json.put("y", 0);
    json.put("moleID", 2);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    json = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    json.put("moleID", 0);
    json.put("x", 2);
    json.put("y", 1);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.DRAWCARD.getPacketType());
    clientThread.sendPacket(new Packet(object));
    object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    json = new JSONObject();
    json.put("x", 2);
    json.put("y", 4);
    json.put("moleID", 0);
    object.put("values", json.toString());
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
