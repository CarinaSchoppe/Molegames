/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra 10 2021
 * File created on 15.11.21, 10:24 by Carina
 * Latest changes made by Carina on 15.11.21, 10:24
 * All contents of "MultiGameHandler" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra 10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra 10.
 */
package de.thundergames.play.util;

import de.thundergames.networking.server.ServerThread;
import de.thundergames.play.game.Game;
import java.io.IOException;
import java.util.HashMap;

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
    games.put(gameID, game);
  }

  public HashMap<Integer, Game> getGames() {
    return games;
  }

  public HashMap<ServerThread, Game> getClientGames() {
    return clientGames;
  }
}
