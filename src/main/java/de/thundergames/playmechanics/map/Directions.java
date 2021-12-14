/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 14.12.21, 15:41 by Carina Latest changes made by Carina on 14.12.21, 15:41 All contents of "Directions" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.map;

/**
 * @author Carina
 * @use the directions a player can move
 */
public enum Directions {
  UP(0),
  DOWN(1),
  LEFT(2),
  RIGHT(3),
  UP_LEFT(4),
  UP_RIGHT(5),
  DOWN_LEFT(6),
  DOWN_RIGHT(7);

  private final int value;

  Directions(final int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
