/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 16:39 by Carina Latest changes made by Carina on 21.12.21, 16:37 All contents of "AusrichterClient" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.Server;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import lombok.Data;

import java.util.HashSet;
import java.util.List;

@Data
public class AusrichterClient {

  private final Server server;
  private float WAIT_TIME = 15;

  public void testGame(final int id) {
    MoleGames.getMoleGames().getGameHandler().createNewGame(id);
    var set1Holes = new HashSet<>(List.of(new Field(1, 2), new Field(0, 0)));
    var set2Holes = new HashSet<>(List.of(new Field(3, 0)));
    var set1DrawAgain = new HashSet<>(List.of(new Field(2, 2), new Field(1, 0)));
    var set2DrawAgain = new HashSet<>(List.of(new Field(2, 0)));
    var floor1 = new Map(set1Holes, set1DrawAgain, 5);
    var floor2 = new Map(set2Holes, set2DrawAgain, 10);
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDGames()
      .get(id)
      .getSettings()
      .getFloors()
      .add(floor1);
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDGames()
      .get(id)
      .getSettings()
      .getFloors()
      .add(floor2);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id).updateGameState();
    //next game
    MoleGames.getMoleGames().getGameHandler().createNewGame(id + 1);
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDGames()
      .get(id + 1)
      .getSettings()
      .getFloors()
      .add(floor1);
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDGames()
      .get(id + 1)
      .getSettings()
      .getFloors()
      .add(floor2);
    MoleGames.getMoleGames().getGameHandler().getIDGames().get(id + 1).updateGameState();
  }

  public Server getServer() {
    return server;
  }

  public void testTournament(final int id) {
    MoleGames.getMoleGames().getGameHandler().createNewTournament(id);
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDTournaments()
      .get(id)
      .getGames()
      .add(new Game(31));
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDTournaments()
      .get(id)
      .getGames()
      .add(new Game(82));
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDTournaments()
      .get(id)
      .getGames()
      .add(new Game(31));
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDTournaments()
      .get(id)
      .getGames()
      .add(new Game(62));
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDTournaments()
      .get(id)
      .getGames()
      .add(new Game(23));
    MoleGames.getMoleGames()
      .getGameHandler()
      .getIDTournaments()
      .get(id)
      .getGames()
      .add(new Game(741));
  }
}
