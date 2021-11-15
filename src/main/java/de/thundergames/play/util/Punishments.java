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
package de.thundergames.play.util;

public enum Punishments {
  NOTHING(0),
  KICK(1),
  POINTS(2);

  private final int id;

  Punishments(final int id) {
    this.id = id;
  }

  /**
   * @param id the id of the punishment
   * @return the punishment with the given id
   * @author Carina
   */
  public static synchronized Punishments getByID(final int id) {
    for (Punishments punishment : Punishments.values()) {
      if (punishment.getID() == id) {
        return punishment;
      }
    }
    return null;
  }

  public int getID() {
    return id;
  }
}
