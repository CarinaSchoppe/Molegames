package de.thundergames.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Floors {

  private final int floorNumber;
  private final int doubleDrawFields;
  private final ArrayList<Hole> holes = new ArrayList<>();
  private final Map map;
  private final int holeAmount;

  public Floors(final int floorNumber, int holes, final int doubleDrawFields, @NotNull final Map map) {
    this.floorNumber = floorNumber;
    this.doubleDrawFields = doubleDrawFields;
    this.map = map;
    this.holeAmount = holes;
  }

  public Map getMap() {
    return map;
  }

  public ArrayList<Hole> getHoles() {
    return holes;
  }

  public int getDoubleDrawFields() {
    return doubleDrawFields;
  }
}
