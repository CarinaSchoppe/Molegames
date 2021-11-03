package game.util;

import network.server.ServerThread;
import network.util.Packet;
import network.util.Packets;

import java.io.IOException;
import java.util.ArrayList;

public class Game extends Thread {

  private final ArrayList<ServerThread> clients = new ArrayList<>();
  private final Punishments punishment;
  private final int gameID;
  private final GameStates currentGameState = GameStates.LOBBY;

  public Game(Punishments punishment, int gameID) {
    this.punishment = punishment;
    this.gameID = gameID;
  }

  @Override
  public void run() {
    //TODO: Run a Game!
    if (currentGameState == GameStates.LOBBY) {
      System.out.println("Starting a game with the gameID: " + gameID);
    }
  }

  public synchronized void joinGame(ServerThread client, boolean spectator) throws IOException {
    clients.add(client);
    client.sendPacket(new Packet(Packets.JOINEDGAME.getPacketType(), gameID));
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
