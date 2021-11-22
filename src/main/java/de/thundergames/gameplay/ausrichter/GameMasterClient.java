/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 22.11.21, 11:24 All contents of "GameMasterClient" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.Server;
import de.thundergames.playmechanics.game.GameStates;
import org.jetbrains.annotations.NotNull;

public class GameMasterClient {

  private final Server server;

  private static GameMasterClient clientInstance;

  public GameMasterClient(@NotNull final Server server) {
    this.server = server;
  }


  public static GameMasterClient getClientInstance() {
    return clientInstance;
  }

  public static void setClientInstance(GameMasterClient clientInstance) {
    GameMasterClient.clientInstance = clientInstance;
  }


  public void test() {
    System.out.println("Test Ausrichter");
    MoleGames.getMoleGames().getGameHandler().createNewGame(0);
    try {

      System.out.println("sleep");
      Thread.sleep(10000);
      System.out.println("sleep done");

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).startGame(GameStates.STARTED);
  }

  public Server getServer() {
    return server;
  }
}
