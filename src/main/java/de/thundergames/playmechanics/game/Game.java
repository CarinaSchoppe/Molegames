/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
 * All contents of "Game" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import com.google.gson.annotations.SerializedName;
import de.thundergames.MoleGames;
import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.ausrichter.ui.MainGUI;
import de.thundergames.gameplay.ausrichter.ui.PlayerManagement;
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
  private final HashSet<Player> eliminatedPlayers = new HashSet<>();
  private final transient HashMap<ServerThread, Player> clientPlayersMap = new HashMap<>();
  private final transient ArrayList<Player> players = new ArrayList<>();
  private final transient ArrayList<Player> spectators = new ArrayList<>();
  private final transient HashMap<Player, Mole> moleMap = new HashMap<>();
  private final transient GameState gameState = new GameState();
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

  public Game(final int gameID) {
    this.gameID = gameID;
  }

  /**
   * @author Carina
   * @use creates a new Game with all settings after the Constructor
   */
  public void create() {
    gameUtil = new GameUtil(this);
    MoleGames.getMoleGames().getGameHandler().getIDGames().put(gameID, this);
    MoleGames.getMoleGames().getGameHandler().getGames().add(this);
    settings = new Settings(this);
    setScore(new Score());
    for (var client : MoleGames.getMoleGames().getServer().getObserver()) {
      MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket(client);
    }
  }

  /**
   * @author Carina
   * @use updates the GameState with the current settings
   */
  public void updateGameState() {
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
      gameState.setFloor(settings.getFloors().get(gameState.getCurrentFloorID()));
    }
    gameState.setPullDiscsOrdered(settings.isPullDiscsOrdered());
    var mappe = new HashMap<Integer, ArrayList<Integer>>();
    for (var players : players) {
      mappe.put(players.getServerClient().getThreadID(), players.getCards());
    }
    gameState.setPullDiscs(mappe);
    gameState.setStatus(currentGameState.getName());
    gameState.setVisualizationTime(settings.getVisualizationTime());
    gameState.setScore(getScore());
    map =
        new Map(
            this,
            gameState.getFloor().getHoles(),
            gameState.getFloor().getDrawAgainFields(),
            gameState.getFloor().getPoints());
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
      for (var client : MoleGames.getMoleGames().getServer().getObserver()) {
        MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket(client);
      }
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println("Starting a game with the gameID: " + getGameID());
      }
      gameUtil.nextPlayer();
      System.out.println(
          "Current player is: "
              + currentPlayer.getServerClient().getThreadID()
              + " name: "
              + currentPlayer.getName());
      MoleGames.getMoleGames()
          .getServer()
          .sendToAllGameClients(
              this,
              MoleGames.getMoleGames().getServer().getPacketHandler().gameStartedPacket(gameState));
      MainGUI.getGUI().updateTable();
    }
  }

  /**
   * @author Carina
   * @use handles when a game ends
   */
  public synchronized void endGame() {
    if (currentGameState != GameStates.NOT_STARTED && currentGameState != GameStates.OVER) {
      if (!getScore().getPoints().isEmpty()) {
        var playerIDs = new ArrayList<>(getScore().getPoints().keySet());
        var max = Collections.max(getScore().getPoints().values());
        for (var playerID : playerIDs) {
          players.add(
              MoleGames.getMoleGames().getServer().getConnectionIDs().get(playerID).getPlayer());
        }
        getScore()
            .getPlayers()
            .sort(
                (o1, o2) ->
                    getScore()
                        .getPoints()
                        .get(o2.getServerClient().getThreadID())
                        .compareTo(getScore().getPoints().get(o1.getServerClient().getThreadID())));
        for (var player : getScore().getPlayers()) {
          if (getScore().getPoints().get(player.getServerClient().getThreadID()).equals(max)) {
            getScore().getWinners().add(player);
          }
        }
        if (MoleGames.getMoleGames().getServer().isDebug()) {
          System.out.println(
              "Server: game with the id: "
                  + getGameID()
                  + " has ended! Winners are: "
                  + getScore().getWinners());
          for (var player : getScore().getPlayers()) {
            System.out.println(
                "Score of player: "
                    + player.getName()
                    + " is: "
                    + getScore().getPoints().get(player.getServerClient().getThreadID()));
          }
        }
        setFinishDateTime(Instant.now().getEpochSecond());
        currentGameState = GameStates.OVER;
        MoleGames.getMoleGames().getServer().getPacketHandler().gameOverPacket(this);
        for (var player : new ArrayList<>(players)) {
          removePlayerFromGame(player);
        }
        for (var player : new ArrayList<>(spectators)) {
          removePlayerFromGame(player);
        }
        updateGameState();
        for (var observer : MoleGames.getMoleGames().getServer().getObserver()) {
          MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket(observer);
        }
      }
      if (MainGUI.getGUI() != null) {
        MainGUI.getGUI().updateTable();
      }
    }
  }

  /**
   * @author Carina
   * @use forces the game to end
   */
  public void forceGameEnd() {
    if (currentGameState != GameStates.NOT_STARTED && currentGameState != GameStates.OVER) {
      MoleGames.getMoleGames().getServer().getPacketHandler().gameCanceledPacket(this);
      endGame();
      MainGUI.getGUI().updateTable();
    }
  }

  /**
   * @author Carina
   * @use pauses the game if needed
   */
  public void pauseGame() {
    if (currentGameState == GameStates.STARTED) {
      MoleGames.getMoleGames().getServer().getPacketHandler().gamePausedPacket(this);
      currentGameState = GameStates.PAUSED;
      updateGameState();
      for (var observer : MoleGames.getMoleGames().getServer().getObserver()) {
        MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket(observer);
      }
      MainGUI.getGUI().updateTable();
    }
  }

  /**
   * @author Carina
   * @use resumes the game
   */
  public void resumeGame() {
    if (currentGameState == GameStates.PAUSED) {
      MoleGames.getMoleGames().getServer().getPacketHandler().gameContinuedPacket(this);
      currentGameState = GameStates.STARTED;
      updateGameState();
      for (var observer : MoleGames.getMoleGames().getServer().getObserver()) {
        MoleGames.getMoleGames().getServer().getPacketHandler().overviewPacket(observer);
      }
      if (!activePlayers.isEmpty()) {
        gameUtil.nextPlayer();
      }
    }
    MainGUI.getGUI().updateTable();
  }

  /**
   * @param client the playerServerThread that joins the game
   * @param spectator if its a spectator or player that has joined
   * @author Carina
   */
  public void joinGame(@NotNull final ServerThread client, final boolean spectator)
      throws NotAllowedError {
    var player = new Player(client).create(this);
    client.setPlayer(player);
    if (getCurrentGameState().equals(GameStates.NOT_STARTED) && !spectator) {
      clientPlayersMap.put(client, player);
      players.add(player);
      activePlayers.add(player);
      getScore().getPlayers().add(player);
      MoleGames.getMoleGames()
          .getGameHandler()
          .getClientGames()
          .put((ServerThread) player.getServerClient(), this);
      updateGameState();
      setCurrentPlayerCount(players.size());
      if (MainGUI.getGUI() != null) {
        MainGUI.getGUI().updateTable();
      }
    } else if (spectator) {
      MoleGames.getMoleGames()
          .getGameHandler()
          .getClientGames()
          .put((ServerThread) player.getServerClient(), this);
      spectators.add(player);
    } else {
      throw new NotAllowedError("Game is over cant be joined anymore!");
    }
    ((ServerThread) player.getServerClient())
        .getServer()
        .getPlayingThreads()
        .add((ServerThread) player.getServerClient());
    ((ServerThread) player.getServerClient())
        .getServer()
        .getLobbyThreads()
        .remove((ServerThread) player.getServerClient());
    if (PlayerManagement.getPlayerManagement() != null) {
      PlayerManagement.getPlayerManagement().updateTable();
    }
  }

  /**
   * @param player the player that has to be removed from the game.
   * @author Carina
   * @use removes all references to the player from the game
   * @use removes all Moles from the Map
   * @see de.thundergames.playmechanics.map.Field
   * @see Map
   * @see Mole
   * @see Player
   */
  public void removePlayerFromGame(@NotNull final Player player) {
    if (player.getServerClient() != null) {
      MoleGames.getMoleGames()
          .getServer()
          .getPlayingThreads()
          .remove((ServerThread) player.getServerClient());
      MoleGames.getMoleGames()
          .getServer()
          .getLobbyThreads()
          .add((ServerThread) player.getServerClient());
    }
    if (currentGameState == GameStates.NOT_STARTED) {
      score.getPlayers().remove(player);
      score.getPoints().remove(player.getServerClient().getThreadID());
    }
    if (currentGameState != GameStates.NOT_STARTED && !currentGameState.equals(GameStates.OVER)) {
      if (!clientPlayersMap.containsKey(player.getServerClient())) {
        eliminatedPlayers.add(player);
      }
    }
    if (activePlayers.contains(player)) {
      clientPlayersMap.get(player.getServerClient()).getTimer().cancel();
      clientPlayersMap.get(player.getServerClient()).setHasMoved(true);
      clientPlayersMap.get(player.getServerClient()).setTimerIsRunning(false);
      for (var moles : player.getMoles()) {
        map.getFieldMap()
            .get(List.of(moles.getField().getX(), moles.getField().getY()))
            .setOccupied(false);
        map.getFieldMap()
            .get(List.of(moles.getField().getX(), moles.getField().getY()))
            .setMole(null);
      }
    }

    player.getMoles().clear();
    clientPlayersMap.remove(player.getServerClient());
    if (currentGameState == GameStates.NOT_STARTED || currentGameState == GameStates.OVER) {
      players.remove(player);
    }
    spectators.remove(player);
    activePlayers.remove(player);
    player.getMoles().clear();
    MoleGames.getMoleGames().getGameHandler().getClientGames().remove(player.getServerClient());
    setCurrentPlayerCount(players.size());
    if (currentGameState == GameStates.NOT_STARTED || currentGameState == GameStates.OVER) {
      ((ServerThread) player.getServerClient())
          .setPlayer(new Player((ServerThread) player.getServerClient()));
    }
    updateGameState();
    if (PlayerManagement.getPlayerManagement() != null) {
      PlayerManagement.getPlayerManagement().updateTable();
    }
    if (MainGUI.getGUI() != null) {
      MainGUI.getGUI().updateTable();
    }
    if (activePlayers.isEmpty()) {
      endGame();
    }
  }

  /** @return gameID with a hashtag in front of it */
  public String getHashtagWithGameID() {
    return "#" + gameID;
  }

  /** @return current player count and the maximum player count with a slash between both */
  public String getCurrentPlayerCount_MaxCount() {
    return currentPlayerCount + "/" + maxPlayerCount;
  }

  /** @return current player count and the maximum player count with a slash between both */
  public String getStatusForTableView() {
    return Objects.equals(status, GameStates.NOT_STARTED.toString()) ? "OPEN" : status;
  }
  // endregion
}
