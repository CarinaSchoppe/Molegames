/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 14:34 by Carina latest changes made by Carina on 06.12.21, 14:33
 * All contents of "AusrichterClient" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.Server;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkFloor;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.playmechanics.game.GameStates;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class AusrichterClient {

  private final Server server;

  public AusrichterClient(@NotNull final Server server) {
    this.server = server;
  }

  public void testGame(final int id) {
    MoleGames.getMoleGames().getGameHandler().createNewGame(id);
    var floor1 = new NetworkFloor();
    var floor2 = new NetworkFloor();
    floor1.setHoles(new HashSet<>(List.of(new NetworkField(8, 11), new NetworkField(2, 6))));
    floor1.setPoints(5);
    floor2.setPoints(10);
    floor1.setDrawAgainFields(new HashSet<>(List.of(new NetworkField(8, 11), new NetworkField(2, 6))));
    floor2.setDrawAgainFields(new HashSet<>(List.of(new NetworkField(8, 11))));
    floor2.setHoles(new HashSet<>(List.of(new NetworkField(0, 0))));
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).getSettings().getFloors().add(floor1);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).getSettings().getFloors().add(floor2);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).updateGameState();
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).startGame(GameStates.STARTED);
  }

  public Server getServer() {
    return server;
  }

  public void testTournament(final int id) {
    MoleGames.getMoleGames().getGameHandler().createNewTournament(id);
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new NetworkGame(31));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new NetworkGame(82));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new NetworkGame(31));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new NetworkGame(62));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new NetworkGame(23));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new NetworkGame(741));
  }
}
