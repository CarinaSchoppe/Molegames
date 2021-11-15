/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra 10 2021
 * File created on 15.11.21, 10:24 by Carina
 * Latest changes made by Carina on 15.11.21, 10:22
 * All contents of "Punishments" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra 10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra 10.
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
