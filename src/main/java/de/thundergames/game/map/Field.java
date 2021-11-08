package de.thundergames.game.map;

public class Field {
  private final int[] id;
  private boolean occupied;
  private boolean hole;
  private boolean doubleMove = false;

  public boolean isDoubleMove() {
    return doubleMove;
  }

  public void setDoubleMove(final boolean doubleMove) {
    this.doubleMove = doubleMove;
  }

  public Field(final int[] id) {
    this.id = id;
  }

  public int[] getId() {
    return id;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public boolean isHole() {
    return hole;
  }

  public void setOccupied(final boolean occupied) {
    this.occupied = occupied;
  }

  public void setHole(final boolean hole) {
    this.hole = hole;
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
