/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.play.map;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Hole {
  final List<Integer> id;
  boolean used;

  public Hole(@NotNull final List<Integer> id, final boolean used) {
    this.id = id;
    this.used = used;
  }
}
