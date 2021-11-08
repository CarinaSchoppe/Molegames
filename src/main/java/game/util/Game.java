package game.util;

import game.map.Map;
import network.server.ServerThread;
import network.util.Packet;
import network.util.Packets;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Game extends Thread {

  private final ArrayList<ServerThread> clients = new ArrayList<>();
  private final Punishments punishment;
  private final int gameID;
  private final Map map;
  private final GameStates currentGameState = GameStates.LOBBY;

  public Game(Punishments punishment, int gameID, int radius, int maxFloors) {
    this.punishment = punishment;
    this.gameID = gameID;
    this.map = new Map(radius, maxFloors);
    map.createMap();
  }

  @Override
  public void run() {
    //TODO: Run a Game!
    if (currentGameState == GameStates.LOBBY) {
      System.out.println("Starting a game with the gameID: " + gameID);
    }
  }

  public void joinGame(ServerThread client, boolean spectator) throws IOException {
    clients.add(client);
    JSONObject object = new JSONObject();
    object.put("type", Packets.JOINEDGAME.getPacketType());
    object.put("gameID", gameID);
    client.sendPacket(new Packet(object));
    MultiGameHandler.getClientGames().put(client, this);
  }

  public ArrayList<ServerThread> getClients() {
    return clients;
  }

  public Punishments getPunishment() {
    return punishment;
  }

  public GameStates getCurrentGameState() {
    return currentGameState;
  }

  public int getGameID() {
    return gameID;
  }

  public int getGameId() {
    return gameID;
  }
}
