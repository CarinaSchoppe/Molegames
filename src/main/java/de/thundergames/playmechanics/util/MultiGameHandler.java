/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "MultiGameHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.game.Game;
import java.io.IOException;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
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
  public void createNewGame(final int gameID, @NotNull final ServerThread ausrichter) {
    if (!games.containsKey(gameID)) {
      var game = new Game(gameID);
      try {
        game.create();
      } catch (IOException e) {
        e.printStackTrace();
      }
      games.put(gameID, game);
    } else {
      ausrichter.sendPacket(new Packet(new JSONObject().put("type", Packets.GAMEEXISTS.getPacketType())));
      System.out.println("Game already exists");
    }
  }

  public HashMap<Integer, Game> getGames() {
    return games;
  }

  public HashMap<ServerThread, Game> getClientGames() {
    return clientGames;
  }
}
