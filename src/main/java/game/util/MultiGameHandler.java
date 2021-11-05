package game.util;

import network.server.ServerThread;

import java.util.HashMap;

/**
 * @author Carina
 * @use the handler that is handling the multi game mechanics that are running by
 * @see Server the server that is using this clas
 * @see ServerThread the thread that is instanciated by the server
 */
public class MultiGameHandler {

  private static final HashMap<Integer, Game> games = new HashMap<>();
  private static final HashMap<ServerThread, Game> clientGames = new HashMap<>();

  private int gameIDs = 0;

  /**
   * @author Carina
   * @param punishment the one that will be set as a default
   * @use creates the new Game
   * @return the game id
   */
  public void createNewGame(Punishments punishment, int radius, int maxFloors) {
    Game game = new Game(punishment, gameIDs, radius, maxFloors);
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
