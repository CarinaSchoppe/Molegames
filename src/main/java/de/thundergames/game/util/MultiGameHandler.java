package de.thundergames.game.util;

import de.thundergames.network.server.ServerThread;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * @author Carina
 * @use the handler that is handling the multi de.thundergames.game mechanics that are running by
 * @see de.thundergames.network.server.Server the server that is using this clas
 * @see ServerThread the thread that is instanciated by the server
 */
public class MultiGameHandler {

  private static final HashMap<Integer, Game> games = new HashMap<>();
  private static final HashMap<ServerThread, Game> clientGames = new HashMap<>();

  private int gameIDs = 0;

  /**
   * @param punishment the one that will be set as a default
   * @return the de.thundergames.game id
   * @author Carina
   * @use creates the new Game
   */
  public void createNewGame(@NotNull Punishments punishment, final int radius, final int maxFloors) {
    var game = new Game(punishment, gameIDs, radius, maxFloors);
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
