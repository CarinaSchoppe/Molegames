/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
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
    games.put(gameID, game);
  }

  public HashMap<Integer, Game> getGames() {
    return games;
  }

  public HashMap<ServerThread, Game> getClientGames() {
    return clientGames;
  }
}
