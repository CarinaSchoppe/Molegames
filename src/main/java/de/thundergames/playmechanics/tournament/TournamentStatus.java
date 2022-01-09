/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 16:05 by Carina Latest changes made by Carina on 09.01.22, 16:05 All contents of "TournamentStatus" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.tournament;

import org.jetbrains.annotations.NotNull;

public enum TournamentStatus {
  NOT_STARTED("NOT_STARTED"),
  STARTED("STARTED"),
  OVER("OVER"),
  INVALID("INVALID");
  private final String status;

  TournamentStatus(@NotNull final String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
