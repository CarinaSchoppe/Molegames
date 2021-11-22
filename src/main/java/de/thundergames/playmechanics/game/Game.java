/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:58 by Carina latest changes made by Carina on 22.11.21, 14:56 All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Game extends NetworkGame {

  private transient GameStates currentGameState = GameStates.NOT_STARTED;
  private transient final HashMap<NetworkPlayer, Player> clientPlayersMap = new HashMap<>();
  private transient final ArrayList<Player> players = new ArrayList<>();
  private transient final HashMap<Player, Mole> moleMap = new HashMap<>();
  private transient Map map;
  private transient Settings settings;
  private transient Player currentPlayer;
  private transient boolean gamePaused = false;
  private transient boolean allMolesPlaced = false;
  private final transient GameState gameState = new GameState();
  private final ArrayList<Player> eliminatedPlayers = new ArrayList<>();

  public Game(int gameID) {
    super(gameID);
  }

  /**
   * @throws IOException
   * @author Carina
   * @use creates a new Game with all settings after the Constructor
   */
  public void create() throws IOException {
    MoleGames.getMoleGames().getGameHandler().getIDGames().put(getGameID(), this);
    settings = new Settings(this);
    updateNetworkGame();
    map = new Map(this);
    updateGameState();
    map.changeFieldParams(gameState);
  }

  /**
   * @author Carina
   * @use updates the GameState with the current settings
   */
  public void updateGameState() {
    gameState.setPlayers(new ArrayList<>(players));
    gameState.setCurrentPlayer(currentPlayer);
    gameState.setPlayers(new ArrayList<>(clientPlayersMap.keySet()));
    var moles = new ArrayList<NetworkMole>();
    for (var players : map.getGame().getMoleMap().keySet()) {
      moles.addAll(players.getMoles());
    }
    gameState.setPlacedMoles(moles);
    gameState.setMoles(settings.getNumberOfMoles());
    gameState.setRadius(settings.getRadius());
    gameState.setFloor(map);
    gameState.setPullDiscsOrdered(settings.isPullDiscsOrdered());
    HashMap<Integer, ArrayList<Integer>> mappe = new HashMap<>();
    for (var players : players) {
      mappe.put(players.getClientID(), players.getCards());
    }
    gameState.setPullDiscs(mappe);
    gameState.setVisualizationTime(settings.getVisualizationTime());
    gameState.setScore(getScore());
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
  public void startGame(GameStates gameState) {
    // TODO: Run a Game!
    if (currentGameState != GameStates.NOT_STARTED || players.isEmpty()) {
      return;
    }
    if (getCurrentGameState() == GameStates.NOT_STARTED) {
      setCurrentGameState(gameState);
      setStartDateTime(Instant.now().getEpochSecond());
      System.out.println("Starting a game with the gameID: " + getGameID());
      nextPlayer();
    }
  }

  public void endGame() {
    setFinishDateTime(Instant.now().getEpochSecond());
    setScore(new Score());
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
   * @use sets the next player in the game if all moles are in holes the player is not on turn
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
    if (allMolesInHoles()) {
      MoleGames.getMoleGames().getServer().sendToAllGameClients(this, MoleGames.getMoleGames().getPacketHandler().playerSkippedPacket(currentPlayer));
      nextPlayer();
    } else {
      if (currentPlayer.getMoles().size() < settings.getNumberOfMoles()) {
        MoleGames.getMoleGames().getServer().sendToAllGameClients(this, MoleGames.getMoleGames().getPacketHandler().playerPlacesMolePacket(currentPlayer.getServerClient()));
      } else {
        MoleGames.getMoleGames().getServer().sendToAllGameClients(this, MoleGames.getMoleGames().getPacketHandler().playersTurnPacket(currentPlayer.getServerClient(), currentPlayer));

      }
      currentPlayer.startThinkTimer();
    }
  }

  public boolean allMolesInHoles() {
    var moleInHoles = 0;
    for (var moles : currentPlayer.getMoles()) {
      for (var hole : map.getHoles()) {
        if (hole.getX() == moles.getField().getX() && hole.getY() == moles.getField().getY()) {
          moleInHoles++;
        }
      }
    }
    return moleInHoles == currentPlayer.getMoles().size();
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
    if (currentGameState != GameStates.NOT_STARTED && !currentGameState.equals(GameStates.OVER)) {
      eliminatedPlayers.add(player);
    }
    for (var moles : player.getMoles()) {
      player.getGame().getMap().getFieldMap().get(List.of(moles.getField().getX(), moles.getField().getY())).setOccupied(false);
    }
    clientPlayersMap.remove(player);
    players.remove(player);
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
