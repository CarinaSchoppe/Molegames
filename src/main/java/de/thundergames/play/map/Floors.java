package de.thundergames.play.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

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

  /**
   * @param floorNumber the floornumber that we are on
   * @param holes amount of holes on that floor
   * @param doubleDrawFields amount of doubleDrawFields on that floor
   * @param map the map itself that this will be placed on
   * @author Carina
   */
  public Floors(
      final int floorNumber, final int holes, final int doubleDrawFields, @NotNull final Map map) {
    this.floorNumber = floorNumber;
    this.doubleDrawFields = doubleDrawFields;
    this.map = map;
    this.holeAmount = holes;
  }

  public Map getMap() {
    return map;
  }

  public ArrayList<Field> getOccupied() {
    return occupied;
  }

  public HashMap<List<Integer>, Field> getFieldMap() {
    return fieldMap;
  }

  public ArrayList<Field> getFields() {
    return fields;
  }
}
