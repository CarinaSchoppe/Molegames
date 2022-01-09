/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 12:04 by Carina Latest changes made by Carina on 09.01.22, 12:04 All contents of "Punishments" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */TION"),
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
}
