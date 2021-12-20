/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 20.12.21, 16:43 by Carina Latest changes made by Carina on 20.12.21, 16:18 All contents of "TournamentState" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.tournament;

import de.thundergames.filehandling.Score;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.util.Player;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

@Getter
@Setter
public class TournamentState {

  private final HashSet<Player> players = new HashSet<>();
  private final HashSet<Game> games = new HashSet<>();
  private final TournamentStatus status;
  private Score score;

  public TournamentState(@NotNull final Score score, @NotNull final TournamentStatus status) {
    this.status = status;
    this.score = score;
  }
}
