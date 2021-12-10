/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 22:18 by Carina latest changes made by Carina on 06.12.21, 22:18
 * All contents of "TournamentState" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

import de.thundergames.filehandling.Score;
import de.thundergames.networking.util.interfaceitems.NetworkGame;
import de.thundergames.networking.util.interfaceitems.NetworkPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class TournamentState {

  private final HashSet<NetworkPlayer> players = new HashSet<>();
  private final HashSet<NetworkGame> games = new HashSet<>();
  private final TournamentStatus status;
  private Score score;

  public TournamentState(@NotNull final Score score, @NotNull final TournamentStatus status) {
    this.status = status;
    this.score = score;
  }

  public TournamentStatus getStatus() {
    return status;
  }

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
