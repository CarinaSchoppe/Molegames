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
package de.thundergames.play.game;

/**
 * @author Carina
 * @use the GameStats that a de.thundergames.game can have
 * @see Game as the class using the GameStates
 */
public enum GameStates {
  LOBBY(0),
  PREGAME(1),
  INGAME(2),
  WINNINGSTATE(3),
  RESETSTATE(4);

  private final int id;

  GameStates(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
