/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 16:08 by Carina
 * Latest changes made by Carina on 15.11.21, 15:58
 * All contents of "Game" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Game {

  private final int gameID;
  private final ArrayList<Player> players = new ArrayList<>();
  private final HashMap<ServerThread, Player> clientPlayersMap = new HashMap<>();
  private final HashMap<Player, Mole> moleMap = new HashMap<>();
  private final HashMap<Integer, Mole> moleIDMap = new HashMap<>();
  private GameStates currentGameState = GameStates.LOBBY;
  private Map map;
  private Settings settings;
  private Player currentPlayer = null;
  private int moleID = 0;
  private boolean gamePaused = false;

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
   * @use starts the game
   */
  public void startGame() {
    // TODO: Run a Game!
    if (currentGameState == GameStates.LOBBY) {
      currentGameState = GameStates.INGAME;
      System.out.println("Starting a game with the gameID: " + gameID);
      nextPlayer();
    }
  }

  /**
   * @author Carina
   * @use forces the game to end
   */
  public void forceGameEnd() {
  }

  public boolean isGamePaused() {
    return gamePaused;
  }

  /**
   * @author Carina
   * @use pauses the game
   */
  public void pauseGame() {
    gamePaused = true;
  }

  /**
   * @author Carina
   * @use resumes the game
   */
  public void resumeGame() {
    gamePaused = false;
    nextPlayer();
  }

  /**
   * @author Carina
   * @use sets the next player in the game
   */
  public void nextPlayer() {
    if (gamePaused) {
      return;
    }
    if (players.size() - 1 >= players.indexOf(currentPlayer) + 1) {
      currentPlayer = players.get(players.indexOf(currentPlayer) + 1);

    } else {
      currentPlayer = players.get(0);
    }
    System.out.println(
        "Current game: "
            + gameID
            + " current-player ID: "
            + currentPlayer.getServerClient().getConnectionId());
    MoleGames.getMoleGames().getPacketHandler().nextPlayerPacket(currentPlayer.getServerClient());
    currentPlayer.startThinkTimer();
  }

  /**
   * @param client    the player that joins the game
   * @param spectator if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final Player client, final boolean spectator) {
    if (currentGameState.equals(GameStates.LOBBY) && !spectator) {
      clientPlayersMap.put(client.getServerClient(), client);
      players.add(client);
      client.getServerClient().sendPacket(PacketHandler.joinedGamePacket(gameID, spectator ? "player" : "spectator"));
      MoleGames.getMoleGames().getGameHandler().getClientGames().put(client.getServerClient(), this);
    } else if (!currentGameState.equals(GameStates.LOBBY) && !spectator) {
      client.getServerClient().sendPacket(new Packet(new JSONObject().put("type", Packets.FULL.getPacketType())));
    }
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
