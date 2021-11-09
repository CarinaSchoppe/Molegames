package de.thundergames.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Floors {

  private final int floorNumber;
  private final int doubleDrawFields;
  private final ArrayList<Hole> holes = new ArrayList<>();
  private final ArrayList<Field> fields = new ArrayList<>();
  private final HashMap<List<Integer>, Field> fieldMap = new HashMap<>();
  private final ArrayList<Field> occupied = new ArrayList<>();
  private final ArrayList<Field> doubleMoves = new ArrayList<>();
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

  public ArrayList<Field> getOccupied() {
    return occupied;
  }

  public HashMap<List<Integer>, Field> getFieldMap() {
    return fieldMap;
  }

  public int getDoubleDrawFields() {
    return doubleDrawFields;
  }

  public ArrayList<Field> getFields() {
    return fields;
  }
}
