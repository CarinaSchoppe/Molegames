/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:49
 * All contents of "Punishments" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

public enum Punishments {
  NOTHING("NOTHING"),
  KICK("KICK"),
  POINTS("POINT_DEDUCTION"),
  NOMOVE("NO_MOVE"),
  INVALIDMOVE("INVALID_MOVE");
  private final String name;

  Punishments(final String name) {
    this.name = name;
  }

  /**
   * @param name the id of the punishment
   * @return the punishment with the given id
   * @author Carina
   */
  public static synchronized Punishments getByName(final String name) {
    for (Punishments punishment : Punishments.values()) {
      if (punishment.getName() == name) {
        return punishment;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }
}
