package game.util;

public enum Punishments {

  NOTHING(0),
  KICK(1),
  POINTS(2);

  private final int id;

  Punishments(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  /**
   * @author Carina
   * @param id that will get the Punishment by id and returns it
   * @return Punishments
   */
  public static  Punishments getPunishmentByID(int id) {
    switch (id) {
      case 0:
        return NOTHING;
      case 1:
        return KICK;
      case 2:
        return POINTS;
    }
    return NOTHING;
  }
}
