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
package de.thundergames.gameplay.player;

/**
 * @author Carina
 * @use the PlayerStates a player can have
 * @use will be changed every time a player can do something
 * @see Player
 */
public enum PlayerStates {
  JOIN(3),
  WAIT(0),
  MOVE(1),
  DRAW(2);

  private final int id;

  PlayerStates(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
