/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 16:26 by Carina Latest changes made by Carina on 15.12.21, 16:26 All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import com.google.gson.annotations.SerializedName;
import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.exceptions.NotAllowedError;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Settings;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class Game {
  private final int gameID;
  private final transient HashMap<ServerThread, Player> clientPlayersMap = new HashMap<>();
  private final transient ArrayList<Player> players = new ArrayList<>();
  private final transient ArrayList<Player> spectators = new ArrayList<>();
  private final transient HashMap<Player, Mole> moleMap = new HashMap<>();
  private final transient GameState gameState = new GameState();
  private final HashSet<Player> eliminatedPlayers = new HashSet<>();
  private final transient ArrayList<Player> activePlayers = new ArrayList<>();
  private int currentPlayerCount;
  private int maxPlayerCount;
  private int levelCount;
  private int moleCount;
  private int radius;
  private boolean pullDiscsOrdered;
  private ArrayList<Integer> pullDiscs = new ArrayList<>();
  private long turnTime;
  private long visualizationTime;
  private String status;
  private String movePenalty;
  private long startDateTime;
  private long finishDateTime;
  @SerializedName(value = "result")
  private Score score;
  private int deductedPoints;
  private transient GameStates currentGameState = GameStates.NOT_STARTED;
  private transient Map map;
  private transient Settings settings;
  private transient Player currentPlayer;
  private transient GameUtil gameUtil;
  private transient int currentFloorID = 0;

  public Game(final int gameID) {
    this.gameID = gameID;
  }
  // TODO: Allgemein umsetzung der Libarysachen mehr oder minder.

  /**
   * @author Carina
   * @use creates a new Game with all settings after the Constructor
   */
  public synchronized void create() {
    gameUtil = new GameUtil(this);
    MoleGames.getMoleGames().getGameHandler().getIDGames().put(gameID, this);
    MoleGames.getMoleGames().getGameHandler().getGames().add(this);
    settings = new Settings(this);
    setScore(new Score());
    for (var client : MoleGames.getMoleGames().getServer().getObserver()) {
      MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket();
    }
  }

  /**
   * @author Carina
   * @use updates the GameState with the current settings
   */
  public synchronized void updateGameState() {
    updateNetworkGame();
    gameState.setActivePlayers(new ArrayList<>(players));
    gameState.setCurrentPlayer(currentPlayer);
    var moles = new ArrayList<Mole>();
    for (var players : activePlayers) {
      moles.addAll(players.getMoles());
    }
    gameState.setPlacedMoles(moles);
    gameState.setMoles(settings.getNumberOfMoles());
    gameState.setRadius(settings.getRadius());
    if (!settings.getFloors().isEmpty()) {
      gameState.setFloor(settings.getFloors().get(currentFloorID));
    }
    gameState.setPullDiscsOrdered(settings.isPullDiscsOrdered());
    var mappe = new HashMap<Integer, ArrayList<Integer>>();
    for (var players : players) {
      mappe.put(players.getClientID(), players.getCards());
    }
    gameState.setPullDiscs(mappe);
    gameState.setStatus(currentGameState.getName());
    gameState.setVisualizationTime(settings.getVisualizationTime());
    gameState.setScore(getScore());
    map = new Map(this, gameState.getFloor().getHoles(), gameState.getFloor().getDrawAgainFields(), gameState.getFloor().getPoints());
    map.build(this);
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
  public void startGame(@NotNull final GameStates gameState) {
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      if (currentGameState != GameStates.NOT_STARTED || activePlayers.isEmpty()) {
        System.out.println("Server: Cant start a game that has no players in it!");
        return;
      }
    }
    if (getCurrentGameState() == GameStates.NOT_STARTED) {
      currentGameState = gameState;
      setStartDateTime(Instant.now().getEpochSecond());
      updateGameState();
      for (var observer : MoleGames.getMoleGames().getServer().getObserver())
        MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket();
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println("Starting a game with the gameID: " + getGameID());
      MoleGames.getMoleGames().getServer().sendToAllGameClients(this, MoleGames.getMoleGames().getServer().getPacketHandler().gameStartedPacket(gameState));
      gameUtil.nextPlayer();
    }
  }

  /**
   * @author Carina
   * @use handles when a game ends
   */
  public synchronized void endGame() {
    setFinishDateTime(Instant.now().getEpochSecond());
    currentGameState = GameStates.OVER;
    updateGameState();
    for (var observer : MoleGames.getMoleGames().getServer().getObserver())
      MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket();
    if (!getScore().getPoints().isEmpty()) {
      var playerIDs = new ArrayList<>(getScore().getPoints().keySet());
      var players = new ArrayList<Player>();
      var max = Collections.max(getScore().getPoints().values());
      for (var playerID : playerIDs) {
        players.add(MoleGames.getMoleGames().getServer().getConnectionIDs().get(playerID).getPlayer());
      }
      Collections.sort(players, (o1, o2) -> getScore().getPoints().get(o2.getClientID()).compareTo(getScore().getPoints().get(o1.getClientID())));
      for (var player : players) {
        if (getScore().getPoints().get(player.getClientID()) == max) {
          getScore().getWinners().add(player);
        }
      }
      if (MoleGames.getMoleGames().getServer().isDebug())
        System.out.println("Server: game with id: " + getGameID() + " has ended! Winners are: " + getScore().getWinners());
      MoleGames.getMoleGames().getServer().getPacketHandler().gameOverPacket(this);
    }
  }

  /**
   * @author Carina
   * @use forces the game to end
   */
  public void forceGameEnd() {
    MoleGames.getMoleGames().getServer().getPacketHandler().gameCanceledPacket(this);
    endGame();
  }

  /**
   * @author Carina
   * @use pauses the game if needed //TODO: pause the game
   */
  public void pauseGame() {
    MoleGames.getMoleGames().getServer().getPacketHandler().gamePausedPacket(this);
    currentGameState = GameStates.PAUSED;
    updateGameState();
    for (var observer : MoleGames.getMoleGames().getServer().getObserver())
      MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket();
  }

  /**
   * @author Carina
   * @use resumes the game
   */
  public void resumeGame() {
    MoleGames.getMoleGames().getServer().getPacketHandler().gameContinuedPacket(this);
    setCurrentGameState(GameStates.STARTED);
    gameUtil.nextPlayer();
  }

  /**
   * @param clientThread the playerServerThread that joins the game
   * @param spectator    if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final ServerThread clientThread, final boolean spectator) throws NotAllowedError {
    var client = new Player(clientThread).create(this);
    clientThread.setPlayer(client);
    MoleGames.getMoleGames()
      .getServer().getPacketHandler()
      .assignToGamePacket(getGameID());
    if (getCurrentGameState().equals(GameStates.NOT_STARTED) && !spectator) {
      clientPlayersMap.put(clientThread, client);
      players.add(client);
      activePlayers.add(client);
      getScore().getPlayers().add(client);
      setCurrentPlayerCount(clientPlayersMap.size());
      MoleGames.getMoleGames()
        .getGameHandler()
        .getClientGames()
        .put((ServerThread) client.getServerClient(), this);
      updateGameState();
    } else if (spectator) {
      MoleGames.getMoleGames()
        .getGameHandler()
        .getClientGames()
        .put((ServerThread) client.getServerClient(), this);
      spectators.add(client);
      //TODO: join as spectator
    } else {
      throw new NotAllowedError("Game is over cant be joined anymore!");
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
  public synchronized void removePlayerFromGame(@NotNull final Player player) {
    if (player == null) {
      return;
    }
    if (activePlayers.contains(player)) {
      if (currentGameState != GameStates.NOT_STARTED && !currentGameState.equals(GameStates.OVER)) {
        if (!clientPlayersMap.containsKey(player)) {
          eliminatedPlayers.add(player);
        }
      }
      MoleGames.getMoleGames().getGameHandler().getClientGames().get(player.getServerClient()).getClientPlayersMap().get(player.getServerClient()).getTimer().cancel();
      MoleGames.getMoleGames().getGameHandler().getClientGames().get(player.getServerClient()).getClientPlayersMap().get(player.getServerClient()).setHasMoved(true);
      MoleGames.getMoleGames().getGameHandler().getClientGames().get(player.getServerClient()).getClientPlayersMap().get(player.getServerClient()).setTimerIsRunning(false);
      for (var moles : player.getMoles()) {
        player
          .getGame()
          .getMap()
          .getFieldMap()
          .get(List.of(moles.getField().getX(), moles.getField().getY()))
          .setOccupied(false);
        player
          .getGame()
          .getMap()
          .getFieldMap()
          .get(List.of(moles.getField().getX(), moles.getField().getY()))
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

  public HashMap<ServerThread, Player> getClientPlayersMap() {
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

  public ArrayList<Player> getSpectators() {
    return spectators;
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

  /**
   * @return gameID with a hashtag in front of it
   */
  public String getHashtagWithGameID() {
    return "#" + gameID;
  }

  /**
   * @return current player count and the maximum player count with a slash between both
   */
  public String getCurrentPlayerCount_MaxCount() {
    return currentPlayerCount + "/" + maxPlayerCount;
  }

  /**
   * @return current player count and the maximum player count with a slash between both
   */
  public String getStatusForTableView() {
    return Objects.equals(status, GameStates.NOT_STARTED.toString()) ? "OPEN" : status;
  }

  //endregion
  public ArrayList<Player> getActivePlayers() {
    return activePlayers;
  }

  public HashSet<Player> getEliminatedPlayers() {
    return eliminatedPlayers;
  }
}
