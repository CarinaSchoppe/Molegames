/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "PlayerStates" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

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
