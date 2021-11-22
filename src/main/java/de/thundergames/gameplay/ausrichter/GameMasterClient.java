/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 21:41 by Carina latest changes made by Carina on 22.11.21, 19:55 All contents of "GameMasterClient" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.Server;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.playmechanics.game.GameStates;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GameMasterClient {

  private static GameMasterClient clientInstance;
  private final Server server;

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
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).getMap().setHoles(new ArrayList<>(List.of(new NetworkField(0, 0), new NetworkField(3, 2), new NetworkField(1, 4))));
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).getMap().setDrawAgainFields(new ArrayList<>(List.of(new NetworkField(4, 0), new NetworkField(4, 2), new NetworkField(4, 4))));
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).updateGameState();
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).getMap().changeFieldParams(MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).getGameState());
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(0).startGame(GameStates.STARTED);
  }

  public Server getServer() {
    return server;
  }
}
