package de.thundergames.play.util;

import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.play.game.Game;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONObject;

/**
 * @author Carina
 * @use the handler that is handling the multi de.thundergames.game mechanics that are running by
 * @see de.thundergames.networking.server.Server the server that is using this clas
 * @see ServerThread the thread that is instanciated by the server
 */
public class MultiGameHandler {

  private static final HashMap<Integer, Game> games = new HashMap<>();
  private static final HashMap<ServerThread, Game> clientGames = new HashMap<>();


  /**
   * @author Carina
   * @use creates the new Game
   */
  public void createNewGame(int gameID) {
    var game = new Game(gameID);
    try {
      game.create();
    } catch (IOException e) {
      e.printStackTrace();
    }
    games.put(gameIDs, game);
    gameIDs++;
  }

  public HashMap<Integer, Game> getGames() {
    return games;
  }

  public HashMap<ServerThread, Game> getClientGames() {
    return clientGames;
  }
}
