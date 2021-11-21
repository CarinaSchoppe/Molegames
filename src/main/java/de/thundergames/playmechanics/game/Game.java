/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.util.interfaceItems.GameState;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

public class Game extends NetworkGame {

  private GameStates currentGameState = GameStates.NOT_STARTED;
  private final HashMap<NetworkPlayer, Player> clientPlayersMap = new HashMap<>();
  private final ArrayList<Player> players = new ArrayList<>();
  private final HashMap<Player, Mole> moleMap = new HashMap<>();
  private Map map;
  private Settings settings;
  private Player currentPlayer;
  private boolean gamePaused = false;
  private boolean allMolesPlaced = false;
  private GameState gameState;

  public Game(int gameID) {
    super(gameID);
  }


  /**
   * @author Carina
   * @use updates the GameState with the current settings
   */
  public void updateGameState() {
    gameState.setCurrentPlayer(currentPlayer);
    gameState.setPlayers(new ArrayList<>(clientPlayersMap.keySet()));
    var moles = new ArrayList<NetworkMole>();
    for (var players : map.getGame().getMoleMap().keySet()) {
      moles.addAll(players.getMoles());
    }
    gameState.setPlacedMoles(moles);
    gameState.setMoles(settings.getNumberOfMoles());
  }

  /**
   * @throws IOException
   * @author Carina
   * @use creates a new Game with all settings after the Constructor
   */
  public void create() throws IOException {
    settings = new Settings(this);
    map = new Map(this);
  }

  public void updateNetworkGame() {
    setMaxPlayerCount(settings.getMaxPlayers());
    setLevelCount(settings.getLevels().size());
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
  public void startGame() {
    // TODO: Run a Game!
    if (getCurrentGameState() == GameStates.NOT_STARTED) {
      setCurrentGameState(GameStates.STARTED);
      setStartDateTime(Instant.now().getEpochSecond());
      System.out.println("Starting a game with the gameID: " + getGameID());
      nextPlayer();
    }
  }

  public void endGame() {
    setFinishDateTime(Instant.now().getEpochSecond());
    setResult(new Score());
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
      currentPlayer = getClientPlayersMap().get(players.get(0));
    }
    //TODO: hier MoleGames.getMoleGames().getPacketHandler().nextPlayerPacket(currentPlayer.getServerClient());
    currentPlayer.startThinkTimer();
  }

  /**
   * @param client    the player that joins the game
   * @param spectator if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final Player client, final boolean spectator) {
    MoleGames.getMoleGames().getPacketHandler().assignToGamePacket(client.getServerClient(), getGameID());
    if (getCurrentGameState().equals(GameStates.NOT_STARTED) && !spectator) {
      clientPlayersMap.put(client, client);
      players.add(client);
      setCurrentPlayerCount(clientPlayersMap.size());
      MoleGames.getMoleGames().getGameHandler().getClientGames().put(client.getServerClient(), this);
    } else if (spectator) {
      //TODO: join as spectator
    }
  }

  public void setOrderOfPlayers(){
    players.clear();

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
    for (var moles : player.getMoles()) {
    }
    clientPlayersMap.remove(player);
    players.remove(player);
    player.getMoles().clear();
    setCurrentPlayerCount(players.size());
  }


  public Settings getSettings() {
    return settings;
  }


  public HashMap<Player, Mole> getMoleMap() {
    return moleMap;
  }

  public HashMap<NetworkPlayer, Player> getClientPlayersMap() {
    return clientPlayersMap;
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

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public void setCurrentGameState(GameStates currentGameState) {
    this.currentGameState = currentGameState;
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setAllMolesPlaced(boolean allMolesPlaced) {
    this.allMolesPlaced = allMolesPlaced;
  }

}
