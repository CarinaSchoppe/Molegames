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
  private Player currentPlayer;

  public Game(final int gameID) {
    this.gameID = gameID;
  }

  public void create() {
    settings = new Settings(this);
    map = new Map(settings.getRadius(), this);
  }

  @Override
  public void run() {
    //TODO: Run a Game!
    map.createMap();
    if (currentGameState == GameStates.LOBBY) {
      System.out.println("Starting a de.thundergames.game with the gameID: " + gameID);
    }
  }

  public void nextPlayer() {
    if (players.get(players.indexOf(currentPlayer) + 1) != null)
      currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
    else currentPlayer = players.get(0);
  }

  public void joinGame(@NotNull final Player client, final boolean spectator) {
    clientPlayersMap.put(client.getServerClient(), client);
    players.add(client);
    MoleGames.getMoleGames().getPacketHandler().joinedGamePacket(client.getServerClient(), gameID);
    MoleGames.getMoleGames().getGameHandler().getClientGames().put(client.getServerClient(), this);
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

  public HashMap<ServerThread, Player> getClientPlayersMap() {
    return clientPlayersMap;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Map getMap() {
    return map;
  }
}
