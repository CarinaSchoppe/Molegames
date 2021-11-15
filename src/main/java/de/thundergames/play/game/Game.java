/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.play.game;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.player.Player;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.play.map.Field;
import de.thundergames.play.map.Map;
import de.thundergames.play.util.Mole;
import de.thundergames.play.util.Settings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

public class Game extends Thread {

  private final int gameID;
  private final GameStates currentGameState = GameStates.LOBBY;
  private final ArrayList<Player> players = new ArrayList<>();
  private final HashMap<ServerThread, Player> clientPlayersMap = new HashMap<>();
  private final HashMap<Player, Mole> moleMap = new HashMap<>();
  private final HashMap<Integer, Mole> moleIDMap = new HashMap<>();
  private Map map;
  private Settings settings;
  private Player currentPlayer = null;
  private int moleID = 0;

  public Game(final int gameID) {
    this.gameID = gameID;
  }

  /**
   * @throws IOException
   * @author Carina
   * @use creates a new Game with all settings after the Constructor
   */
  public void create() throws IOException {
    settings = new Settings(this);
    map = new Map(settings.getRadius(), this);
  }

  /**
   * @author Carina
   * @use starts the current game
   */
  @Override
  public void run() {
    // TODO: Run a Game!
    if (currentGameState == GameStates.LOBBY) {
      System.out.println("Starting a game with the gameID: " + gameID);
      nextPlayer();
    }
  }

  /**
   * @author Carina
   * @use sets the next player in the game
   */
  public void nextPlayer() {
    if (players.size() - 1 >= players.indexOf(currentPlayer) + 1) {
      currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
    } else currentPlayer = players.get(0);
    currentPlayer.startThinkTimer();
    System.out.println(
        "Current game: "
            + gameID
            + " current-player ID: "
            + currentPlayer.getServerClient().getConnectionId());
  }

  /**
   * @param client the player that joins the game
   * @param spectator if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final Player client, final boolean spectator) {
    clientPlayersMap.put(client.getServerClient(), client);
    players.add(client);
    MoleGames.getMoleGames().getPacketHandler().joinedGamePacket(client.getServerClient(), gameID);
    MoleGames.getMoleGames().getGameHandler().getClientGames().put(client.getServerClient(), this);
  }

  /**
   * @param player the player that has to be removed from the game.
   * @author Carina
   * @use removes all references to the player from the game
   * @use removes all Moles from the Map
   * @see Field
   * @see Map
   * @see Mole
   * @see Player
   */
  public void removePlayerFromGame(@NotNull final Player player) {
    for (var field : map.getFloor().getFields()) {
      if (field.getMole() != null && field.getMole().getPlayer().equals(player)) {
        field.setOccupied(false, null);
        map.getFloor().getOccupied().remove(field);
        player.getMoles().clear();
        players.remove(player);
        clientPlayersMap.remove(player.getServerClient());
      }
    }
  }

  public ArrayList<Player> getClients() {
    return players;
  }

  public Settings getSettings() {
    return settings;
  }

  public GameStates getCurrentGameState() {
    return currentGameState;
  }

  public HashMap<Integer, Mole> getMoleIDMap() {
    return moleIDMap;
  }

  public HashMap<Player, Mole> getMoleMap() {
    return moleMap;
  }

  public HashMap<ServerThread, Player> getClientPlayersMap() {
    return clientPlayersMap;
  }

  public int getMoleID() {
    return moleID;
  }

  public synchronized void setMoleID(int moleID) {
    this.moleID = moleID;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public int getGameID() {
    return gameID;
  }
}
