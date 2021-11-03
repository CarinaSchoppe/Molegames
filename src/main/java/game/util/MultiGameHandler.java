package game.util;

import network.server.ServerThread;

import java.util.HashMap;

public class MultiGameHandler {

  private static final HashMap<Integer, Game> games = new HashMap<>();
  private static final HashMap<ServerThread, Game> clientGames = new HashMap<>();

  private int gameIDs = 0;

  public void createNewGame(Punishments punishment) {
    Game game = new Game(punishment, gameIDs);
    games.put(gameIDs, game);
    gameIDs++;
    game.start();
  }

  public int getGameIDs() {
    return gameIDs;
  }

  public static HashMap<Integer, Game> getGames() {
    return games;
  }

  public static HashMap<ServerThread, Game> getClientGames() {
    return clientGames;
  }
}
