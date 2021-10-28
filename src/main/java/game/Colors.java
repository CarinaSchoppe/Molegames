package game;

/**
 * @author Carina
 */
public enum Colors {

  BLUE(0),
  RED(1),
  GREEN(2),
  YELLOW(3),
  BLACK(4),
  WHITE(5);

  final int id;

  /**
   * @param id the Id of the color
   * @author Carina
   */
  Colors(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
