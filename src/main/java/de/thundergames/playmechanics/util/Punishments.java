/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 13.01.22, 16:58 by Carina Latest changes made by Carina on 13.01.22, 16:58 All contents of "Punishments" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
  public static Punishments getByName(final String name) {
    for (var punishment : Punishments.values()) {
      if (punishment.getName().equals(name)) {
        return punishment;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
