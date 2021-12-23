/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.12.21, 16:55 by Carina Latest changes made by Carina on 23.12.21, 16:55 All contents of "Client" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.player;

import de.thundergames.gameplay.player.networking.ClientPacketHandler;
import de.thundergames.gameplay.player.networking.ClientThread;
import de.thundergames.networking.util.Network;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.tournament.Tournament;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

@Getter
@Setter
public class Client extends Network {

  protected static Client CLIENT;
  public final String name;
  private final HashSet<Game> games = new HashSet<>();
  private final HashSet<Tournament> tournaments = new HashSet<>();
  private final ArrayList<Integer> pullDiscs = new ArrayList<>();
  private final ArrayList<Mole> moles = new ArrayList<>();
  protected ClientPacketHandler clientPacketHandler;
  protected ClientThread clientThread;
  private GameState gameState;
  private long remainingTime;
  private Map map;
  private Player currentPlayer;
  private Player player;
  private boolean isDraw = false;
  private int gameID;

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
  }

  public static void main(String[] args) {
    var client = new Client(5000, "localhost", "Carina");
    client.clientPacketHandler.joinGamePacket(0, true);
  }

  public static Client getClientInstance() {
    return CLIENT;
  }

  public String getName() {
    return name;
  }

  /**
   * @author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we
   * create the Constructor and call the create object to create the sockets and stream
   * @see Client
   */
  @Override
  public void create() {
    CLIENT = this;
    clientPacketHandler = new ClientPacketHandler(this);
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
      clientPacketHandler.loginPacket(name);
    } catch (IOException exception) {
      if (isDebug()) System.out.println("Is the server running?!");
    }
  }
}
