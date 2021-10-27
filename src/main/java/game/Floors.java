package game;

public enum Floors {

  FIRST(""),
  SECOND(""),
  THIRD(""),
  FOURTH(""),
  FIVETH("");

  String path;

  /**
   * @author Carina
   * @param path to the File for this Floor
   */
  Floors(String path) {
    this.path = path;
  }
}
