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
