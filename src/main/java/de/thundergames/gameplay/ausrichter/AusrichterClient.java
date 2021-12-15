/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 16:25 by Carina Latest changes made by Carina on 15.12.21, 16:25 All contents of "AusrichterClient" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.Server;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class AusrichterClient {

  private final Server server;

  public void testGame(final int id) {
    MoleGames.getMoleGames().getGameHandler().createNewGame(id);
    var set1new = new HashSet<>(List.of(new Field(8, 11), new Field(2, 6)));
    var set2new = new HashSet<>(List.of(new Field(0, 0)));
    var floor1 = new Map(set1new, set1new, 5);
    var floor2 = new Map(set2new, set2new, 10);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).getSettings().getFloors().add(floor1);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).getSettings().getFloors().add(floor2);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).updateGameState();
    try {
      Thread.sleep(5000);
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
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new Game(31));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new Game(82));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new Game(31));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new Game(62));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new Game(23));
    MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(id).getGames().add(new Game(741));
  }
}
