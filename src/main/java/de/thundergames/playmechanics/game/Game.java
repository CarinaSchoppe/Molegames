/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 18:17 by Carina latest changes made by Carina on 02.12.21, 18:17
 * All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Game extends NetworkGame {

  private final transient HashMap<NetworkPlayer, Player> clientPlayersMap = new HashMap<>();
  private final transient ArrayList<Player> players = new ArrayList<>();
  private final transient HashMap<Player, Mole> moleMap = new HashMap<>();
  private final transient GameState gameState = new GameState();
  private final HashSet<Player> eliminatedPlayers = new HashSet<>();
  private final transient ArrayList<Player> activePlayers = new ArrayList<>();
  private final transient boolean allMolesPlaced = false;
  private transient GameStates currentGameState = GameStates.NOT_STARTED;
  private transient Map map;
  private transient Settings settings;
  private transient Player currentPlayer;
  private transient boolean gamePaused = false;
  private transient GameUtil gameUtil;
  private transient int currentFloorID = 0;

  public Game(int gameID) {
    super(gameID);
  }

  /**
   * @throws IOException
   * @author Carina
   * @use creates a new Game with all settings after the Constructor
   */
  public void create() throws IOException {
    gameUtil = new GameUtil(this);
    MoleGames.getMoleGames().getGameHandler().getIDGames().put(getGameID(), this);
    MoleGames.getMoleGames().getGameHandler().getGames().add(this);
    for (var client : MoleGames.getMoleGames().getServer().getObserver()) {
      MoleGames.getMoleGames().getPacketHandler().overviewPacket(client);
    }
    settings = new Settings(this);
    setScore(new Score());
  }

  /**
   * @author Carina
   * @use updates the GameState with the current settings
   */
  public void updateGameState() {
    updateNetworkGame();
    map = new Map(this);
    gameState.setPlayers(new ArrayList<>(activePlayers));
    gameState.setCurrentPlayer(currentPlayer);
    var moles = new ArrayList<NetworkMole>();
    for (var players : activePlayers) {
      moles.addAll(players.getMoles());
    }
    gameState.setPlacedMoles(moles);
    gameState.setMoles(settings.getNumberOfMoles());
    gameState.setRadius(settings.getRadius());
    gameState.setFloor(settings.getFloors().get(currentFloorID));
    gameState.setPullDiscsOrdered(settings.isPullDiscsOrdered());
    HashMap<Integer, ArrayList<Integer>> mappe = new HashMap<>();
    for (var players : players) {
      mappe.put(players.getClientID(), players.getCards());
    }
    gameState.setPullDiscs(mappe);
    gameState.setVisualizationTime(settings.getVisualizationTime());
    gameState.setScore(getScore());
    map.setHoles(gameState.getFloor().getHoles());
    map.setDrawAgainFields(gameState.getFloor().getDrawAgainFields());
    map.changeFieldParams(gameState);
  }

  /**
   * @author Carina
   * @use handles the update of the NetworkGame stuff (super class)
   */
  public void updateNetworkGame() {
    setMaxPlayerCount(settings.getMaxPlayers());
    setLevelCount(settings.getFloors().size());
    setMoleCount(settings.getNumberOfMoles());
    setRadius(settings.getRadius());
    setPullDiscsOrdered(settings.isPullDiscsOrdered());
    setPullDiscs(settings.getPullDiscs());
    setTurnTime(settings.getTurnTime());
    setVisualizationTime(settings.getVisualizationTime());
    setStatus(currentGameState.getName());
    setMovePenalty(settings.getPunishment().getName());
  }

  /**
   * @author Carina
   * @use starts the game
   */
  public void startGame(GameStates gameState) {
    if (currentGameState != GameStates.NOT_STARTED || players.isEmpty()) {
      System.out.println("Server: Cant start a game that has no players in it!");
      return;
    }
    if (getCurrentGameState() == GameStates.NOT_STARTED) {
      setCurrentGameState(gameState);
      setStartDateTime(Instant.now().getEpochSecond());
      updateNetworkGame();
      System.out.println("Starting a game with the gameID: " + getGameID());
      gameUtil.nextPlayer();
    }
  }

  /**
   * @author Carina
   * @use handles when a game ends
   */
  public void endGame() {
    setFinishDateTime(Instant.now().getEpochSecond());
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
    gameUtil.nextPlayer();
  }

  /**
   * @param client the player that joins the game
   * @param spectator if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final Player client, final boolean spectator) {
    MoleGames.getMoleGames()
        .getPacketHandler()
        .assignToGamePacket(client.getServerClient(), getGameID());
    if (getCurrentGameState().equals(GameStates.NOT_STARTED) && !spectator) {
      clientPlayersMap.put(client, client);
      players.add(client);
      activePlayers.add(client);
      getScore().getPlayers().add(client);
      setCurrentPlayerCount(clientPlayersMap.size());
      MoleGames.getMoleGames()
          .getGameHandler()
          .getClientGames()
          .put(client.getServerClient(), this);
    } else if (spectator) {
      // TODO: join as spectator
    }
    updateGameState();
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
    // TODO: check if player was really removed
    if (currentGameState != GameStates.NOT_STARTED && !currentGameState.equals(GameStates.OVER)) {
      if (!clientPlayersMap.containsKey(player)) {
        eliminatedPlayers.add(player);
      }
    }
    for (var moles : player.getMoles()) {
      player
          .getGame()
          .getMap()
          .getFieldMap()
          .get(List.of(moles.getNetworkField().getX(), moles.getNetworkField().getY()))
          .setOccupied(false);
      player
          .getGame()
          .getMap()
          .getFieldMap()
          .get(List.of(moles.getNetworkField().getX(), moles.getNetworkField().getY()))
          .setMole(null);
    }
    player.getMoles().clear();
    clientPlayersMap.remove(player);
    players.remove(player);
    activePlayers.remove(player);
    player.getMoles().clear();
    MoleGames.getMoleGames().getGameHandler().getClientGames().remove(player.getServerClient());
    setCurrentPlayerCount(players.size());
    updateGameState();
  }

  public Settings getSettings() {
    return settings;
  }

  public HashMap<Player, Mole> getMoleMap() {
    return moleMap;
  }

  public GameUtil getGameUtil() {
    return gameUtil;
  }

  public HashMap<NetworkPlayer, Player> getClientPlayersMap() {
    return clientPlayersMap;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public GameStates getCurrentGameState() {
    return currentGameState;
  }

  public void setCurrentGameState(GameStates currentGameState) {
    this.currentGameState = currentGameState;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public int getCurrentFloorID() {
    return currentFloorID;
  }

  public void setCurrentFloorID(int currentFloorID) {
    this.currentFloorID = currentFloorID;
  }

  public GameState getGameState() {
    return gameState;
  }

  public ArrayList<Player> getActivePlayers() {
    return activePlayers;
  }

  public HashSet<Player> getEliminatedPlayers() {
    return eliminatedPlayers;
  }
}
