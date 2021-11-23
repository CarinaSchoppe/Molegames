/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 14:33 by Carina latest changes made by Carina on 23.11.21, 14:33 All contents of "TournamentState" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import de.thundergames.filehandling.Score;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import java.util.HashSet;

public class TournamentState {

  private final HashSet<NetworkPlayer> players = new HashSet<>();
  private final HashSet<NetworkGame> games = new HashSet<>();
  private Score score;

  public HashSet<NetworkPlayer> getPlayers() {
    return players;
  }

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }

  public HashSet<NetworkGame> getGames() {
    return games;
  }
}
