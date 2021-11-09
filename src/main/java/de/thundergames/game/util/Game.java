package de.thundergames.game.util;

import de.thundergames.MoleGames;
import de.thundergames.game.map.Map;
import de.thundergames.network.server.ServerThread;
import de.thundergames.network.util.Packet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Game extends Thread {

  private final ArrayList<ServerThread> clients = new ArrayList<>();
  private final Punishments punishment;
  private final int gameID;
  private final Map map;
  private int[] cards;
  private final GameStates currentGameState = GameStates.LOBBY;

  public Game(@NotNull final Punishments punishment, final int gameID, final int radius, final int maxFloors) {
    this.punishment = punishment;
    this.gameID = gameID;
    this.map = new Map(radius, maxFloors);
    map.createMap();
  }

  @Override
  public void run() {
    //TODO: Run a Game!
    if (currentGameState == GameStates.LOBBY) {
      System.out.println("Starting a de.thundergames.game with the gameID: " + gameID);
    }
  }

  public void joinGame(@NotNull final ServerThread client, final boolean spectator) {
    clients.add(client);
    MoleGames.getMoleGames().getPacketHandler().joinedGamePacket(client, gameID);
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

  public void madeAMove(ServerThread player, Packet packet) {
  }

  public int[] getCards() {
    return cards;
  }

  public void setCards(int[] cards) {
    this.cards = cards;
  }
}
