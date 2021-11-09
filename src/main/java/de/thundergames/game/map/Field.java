package de.thundergames.game.map;

public class Field {
  private final int[] id;
  private boolean occupied;
  private boolean hole;
  private boolean doubleMove = false;

  public Field(final int[] id) {
    this.id = id;
  }

  public int[] getId() {
    return id;
  }

  @Override
  public String toString() {
    return "field x: " + id[0] + " y: " + id[1];
  }

  public int getY() {
    return id[1];
  }

  public int getX() {
    return id[0];
  }
}
