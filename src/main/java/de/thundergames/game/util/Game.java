package de.thundergames.game.util;

import de.thundergames.MoleGames;
import de.thundergames.game.map.Map;
import de.thundergames.network.server.ServerThread;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Thread {

  private final int gameID;
  private Map map;
  private Settings settings;
  private final GameStates currentGameState = GameStates.LOBBY;
  private final ArrayList<Player> players = new ArrayList<>();
  private final HashMap<ServerThread, Player> clientPlayersMap = new HashMap<>();
  private final HashMap<Player, Mole> moleMap = new HashMap<>();
  private final HashMap<Integer, Mole> moleIDMap = new HashMap<>();
  private Player currentPlayer = null;
  private int moleID = 0;

  public Game(final int gameID) {
    this.gameID = gameID;
  }

  public synchronized void create() {
    settings = new Settings(this);
    map = new Map(settings.getRadius(), this);
  }

  @Override
  public void run() {
    //TODO: Run a Game!
    if (currentGameState == GameStates.LOBBY) {
      System.out.println("Starting a game with the gameID: " + gameID);
      nextPlayer();
    }
  }

  public synchronized void nextPlayer() {
    if (players.size() - 1 >= players.indexOf(currentPlayer) + 1) {
      currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
    } else currentPlayer = players.get(0);
    currentPlayer.startThinkTimer();
    System.out.println("Current game: " + gameID + " current-player ID: " + currentPlayer.getServerClient().getConnectionId());
  }

  public synchronized void joinGame(@NotNull final Player client, final boolean spectator) {
    clientPlayersMap.put(client.getServerClient(), client);
    players.add(client);
    MoleGames.getMoleGames().getPacketHandler().joinedGamePacket(client.getServerClient(), gameID);
    MoleGames.getMoleGames().getGameHandler().getClientGames().put(client.getServerClient(), this);
  }

  public void performPunishment() {
    if (!settings.getPunishment().equals(Punishments.NOTHING)) {
    }
  }

  public void removePlayerFromGame(Player player) {
    players.remove(player);
    clientPlayersMap.remove(player.getServerClient());
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

  public void setMap(Map map) {
    this.map = map;
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
}
