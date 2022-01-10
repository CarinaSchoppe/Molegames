/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:45 by Carina Latest changes made by Carina on 09.01.22, 21:45 All contents of "MultiGameHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.tournament.Tournament;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Carina
 * @use the handler that is handling the multi de.thundergames.game mechanics that are running by
 * @see de.thundergames.networking.server.Server the server that is using this clas
 * @see ServerThread the thread that is instanciated by the server
 */
@Data
public class MultiGameHandler {

  private final GameLogic gameLogic = new GameLogic();
  private final HashSet<Game> games = new HashSet<>();
  private final HashSet<Tournament> tournaments = new HashSet<>();
  private final HashMap<Integer, Game> IDGames = new HashMap<>();
  private final HashMap<Integer, Tournament> IDTournaments = new HashMap<>();
  private final HashMap<ServerThread, Game> clientGames = new HashMap<>();
  private final HashMap<ServerThread, Tournament> clientTournaments = new HashMap<>();

  /**
   * @author Carina
   * @use creates the new game
   */
  public void createNewGame(final int gameID) {
    if (!IDGames.containsKey(gameID)) {
      new Game(gameID).create();
    } else {
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println("Game already exists");
      }
    }
  }

  /**
   * @author Carina
   * @use creates the new tournament
   */
  public void createNewTournament(final int tournamentID) {
    if (!IDGames.containsKey(tournamentID)) {
      var tournament = new Tournament(tournamentID);
      tournament.create();
    } else {
      if (MoleGames.getMoleGames().getServer().isDebug()) {
        System.out.println("Tournament already exists");
      }
    }
  }
}
