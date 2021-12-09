/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 22:18 by Carina latest changes made by Carina on 06.12.21, 22:17
 * All contents of "TournamentStatus" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.game;

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
