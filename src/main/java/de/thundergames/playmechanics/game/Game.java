/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Datetime;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Game   {
  private GameStates currentGameState = GameStates.NOT_STARTED;

  private final ArrayList<Player> players = new ArrayList<>();
  private final HashMap<ServerThread, Player> clientPlayersMap = new HashMap<>();
  private final HashMap<Player, Mole> moleMap = new HashMap<>();
  private final HashMap<Integer, Mole> moleIDMap = new HashMap<>();
  private final ArrayList<ServerThread> AIs = new ArrayList<>();
  private Map map;
  private Settings settings;
  private Player currentPlayer = null;
  private int moleID = 0;
  private boolean gamePaused = false;
  private boolean allMolesPlaced = false;
  private final int gameID;
  private int currentPlayerCount;
  private Datetime startDateTime;
  private Datetime finishDateTime;
  private Score result;
  public Game(int gameID) {
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
    if (getCurrentGameState() == GameStates.NOT_STARTED) {
      setCurrentGameState(GameStates.STARTED);
      setStartDateTime(new Datetime());
      System.out.println("Starting a game with the gameID: " + getGameID());
      nextPlayer();
    }
  }

  public void endGame(){
    setFinishDateTime(new Datetime());
  }

  /**
   * @author Carina
   * @use forces the game to end
   */
  public void forceGameEnd() {
    endGame();
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
    MoleGames.getMoleGames().getPacketHandler().nextPlayerPacket(currentPlayer.getServerClient());
    currentPlayer.startThinkTimer();
  }

  /**
   * @param client    the player that joins the game
   * @param spectator if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final Player client, final boolean spectator) {
    if (getCurrentGameState().equals(GameStates.NOT_STARTED) && !spectator) {
      clientPlayersMap.put(client.getServerClient(), client);
      players.add(client);
      setCurrentPlayerCount(players.size());
      for (var connection : getAIs()) {
        getMap().sendMap(connection);
      }
      client.getServerClient().sendPacket(PacketHandler.joinedGamePacket(getGameID(), spectator ? "player" : "spectator"));
      MoleGames.getMoleGames().getGameHandler().getClientGames().put(client.getServerClient(), this);
    } else if (!getCurrentGameState().equals(GameStates.NOT_STARTED) && !spectator) {
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
      if (field.getFloor().getMap().getGame().getMoleIDMap().get(field.getMole()) != null && field.getFloor().getMap().getGame().getMoleIDMap().get(field.getMole()).getPlayer().equals(player)) {
        field.setOccupied(false, -1);
        map.getFloor().getOccupied().remove(field);
        player.getMoles().clear();
        players.remove(player);
        setCurrentPlayerCount(players.size());
        clientPlayersMap.remove(player.getServerClient());
      }
    }
  }

  public String toJsonObject(){
    var jsonObject = new JSONObject();
    jsonObject.put("gameID", getGameID());
    jsonObject.put("currentPlayerCount", getCurrentPlayerCount());
    jsonObject.put("maxPlayerCount", settings.getMaxPlayers());
    jsonObject.put("status", getCurrentGameState().getName());
    jsonObject.put("startDateTime", getStartDateTime().toString());
    jsonObject.put("finishDateTime", getFinishDateTime().toString());
    jsonObject.put("result", result.toJsonObject());
    return jsonObject.toString();
  }



  public ArrayList<Player> getClients() {
    return players;
  }

  public Settings getSettings() {
    return settings;
  }


  public HashMap<Integer, Mole> getMoleIDMap() {
    return moleIDMap;
  }

  public HashMap<Player, Mole> getMoleMap() {
    return moleMap;
  }

  public ArrayList<ServerThread> getAIs() {
    return AIs;
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

  public boolean isAllMolesPlaced() {

    return allMolesPlaced;
  }

  public GameStates getCurrentGameState() {
    return currentGameState;
  }


  public void setCurrentGameState(GameStates currentGameState) {
    this.currentGameState = currentGameState;
  }


  public int getCurrentPlayerCount() {
    return currentPlayerCount;
  }

  public void setCurrentPlayerCount(int currentPlayerCount) {
    this.currentPlayerCount = currentPlayerCount;
  }

  public Score getResult() {
    return result;
  }

  public void setResult(Score result) {
    this.result = result;
  }

  public Datetime getFinishDateTime() {
    return finishDateTime;
  }

  public void setFinishDateTime(Datetime finishDateTime) {
    this.finishDateTime = finishDateTime;
  }

  public Datetime getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(Datetime startDateTime) {
    this.startDateTime = startDateTime;
  }


  public int getGameID() {
    return gameID;
  }




  public void setAllMolesPlaced(boolean allMolesPlaced) {
    this.allMolesPlaced = allMolesPlaced;
  }

}
