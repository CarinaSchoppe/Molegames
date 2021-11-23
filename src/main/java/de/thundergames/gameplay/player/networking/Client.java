/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 13:45 by Carina latest changes made by Carina on 23.11.21, 13:45 All contents of "Client" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import de.thundergames.networking.util.Network;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.map.Map;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import org.jetbrains.annotations.NotNull;

public class Client extends Network {

  private static final boolean keyListener = true;
  protected static Client client;
  private final String name;
  private final HashSet<NetworkGame> games = new HashSet<>();
  private final HashSet<NetworkGame> tournaments = new HashSet<>();
  private final ArrayList<Integer> pullDiscs = new ArrayList<>();
  protected ClientPacketHandler clientPacketHandler;
  protected ClientThread clientThread;
  private final ArrayList<NetworkMole> moles = new ArrayList<>();
  private GameState gameState;
  private int gameID;
  private long remainingTime;
  private Map map;
  private NetworkPlayer networkPlayer;
  private boolean isDraw = false;


  /**
   * @param port
   * @param ip
   * @param name
   * @author Carina
   * @use creates a basic client instance
   */
  public Client(final int port, @NotNull final String ip, @NotNull final String name) {
    super(port, ip);
    this.name = name;
    clientPacketHandler = new ClientPacketHandler();
  }

  public static void main(String[] args) {
    Client client = new Client(5000, "localhost", "Carina");
    client.create();
    client.clientPacketHandler.joinGamePacket(client, 0, true);
  }

  public static boolean isKeyListener() {
    return keyListener;
  }

  public static Client getClient() {
    return client;
  }

  public String getName() {
    return name;
  }

  /**
   * @author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we create the Constructor and call the create object to create the sockets and stream
   * @see Client
   */
  @Override
  public void create() {
    client = this;
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
      clientPacketHandler.loginPacket(client, name);
    } catch (IOException exception) {
      System.out.println("Is the server running?!");
    }
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public ClientPacketHandler getClientPacketHandler() {
    return clientPacketHandler;
  }

  public ClientThread getClientThread() {
    return clientThread;
  }

  public void setGameID(final int gameID) {
    this.gameID = gameID;
  }

  public HashSet<NetworkGame> getGames() {
    return games;
  }

  public long getRemainingTime() {
    return remainingTime;
  }

  public void setRemainingTime(long remainingTime) {
    this.remainingTime = remainingTime;
  }

  public de.thundergames.playmechanics.map.Map getMap() {
    return map;
  }

  public void setMap(de.thundergames.playmechanics.map.Map map) {
    this.map = map;
  }

  public NetworkPlayer getNetworkPlayer() {
    return networkPlayer;
  }

  public void setNetworkPlayer(NetworkPlayer networkPlayer) {
    this.networkPlayer = networkPlayer;
  }

  public boolean isDraw() {
    return isDraw;
  }

  public void setDraw(boolean draw) {
    isDraw = draw;
  }

  public ArrayList<Integer> getPullDiscs() {
    return pullDiscs;
  }

  public HashSet<NetworkGame> getTournaments() {
    return tournaments;
  }

  public ArrayList<NetworkMole> getMoles() {
    return moles;
  }
}
