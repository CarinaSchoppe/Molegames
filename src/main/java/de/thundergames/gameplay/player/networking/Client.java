/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 15:19 by Carina latest changes made by Carina on 21.11.21, 15:12 All contents of "Client" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player.networking;

import de.thundergames.networking.util.Network;
import de.thundergames.networking.util.interfaceItems.GameState;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class Client extends Network {

  private static final boolean keyListener = true;
  protected static Client client;
  private final ArrayList<Integer> moleIDs = new ArrayList<>();
  protected ClientPacketHandler clientPacketHandler;
  protected ClientThread clientThread;
  private final String name;
  private GameState gameState;
  private int id;
  private int gameID;
  private final ArrayList<NetworkGame> games = new ArrayList<>();
  private final ArrayList<NetworkGame> tournaments = new ArrayList<>();

  public static void main(String[] args) {
    Client client = new Client(5000, "localhost", "Carina");
    client.create();

    //TODO: testing
  }



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

  public void setClientID(final int id) {
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

  public ArrayList<NetworkGame> getGames() {
    return games;
  }

  public ArrayList<NetworkGame> getTournaments() {
    return tournaments;
  }
}
