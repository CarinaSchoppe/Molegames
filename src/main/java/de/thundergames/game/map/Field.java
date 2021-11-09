package de.thundergames.game.map;

import java.util.List;

public class Field {
  private final List<Integer> id;
  private boolean occupied;
  private boolean hole;
  private final boolean doubleMove = false;

  public Field(final List<Integer> id) {
    this.id = id;
  }

  public List<Integer> getId() {
    return id;
  }

  @Override
  public String toString() {
    return "field x: " + id.get(0) + " y: " + id.get(1);
  }

  public int getY() {
    return id.get(1);
  }

  public int getX() {
    return id.get(0);
  }

  public void setOccupied(final boolean occupied) {
    this.occupied = occupied;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public boolean isHole() {
    return hole;
  }
}
