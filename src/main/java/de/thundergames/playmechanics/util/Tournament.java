/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 16:22 by Carina latest changes made by Carina on 22.11.21, 16:20 All contents of "Tournament" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import de.thundergames.filehandling.Score;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import java.util.ArrayList;

public class Tournament {

  private final int tournamentID;
  private int playerCount;
  private ArrayList<NetworkGame> games;
  private Score score;

  public Tournament(int tournamentID) {
    this.tournamentID = tournamentID;
  }


  public void create() {
  }

  public int getTournamentID() {
    return tournamentID;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public ArrayList<NetworkGame> getGames() {
    return games;
  }

  public void setGames(ArrayList<NetworkGame> games) {
    this.games = games;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }
}
