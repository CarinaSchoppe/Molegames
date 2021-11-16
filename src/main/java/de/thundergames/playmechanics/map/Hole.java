/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "Hole" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

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
