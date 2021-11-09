package de.thundergames.game.map;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

  private final int radius;
  private final int maxFloors;
  private Floors floor;
  private int currentFloor;
  private int holeAmount;
  private int doubleDrawFields;

  public Map(final int radius, final int maxFloors) {
    this.radius = radius + 1;
    this.maxFloors = maxFloors;
    currentFloor = maxFloors;
  }

  private final ArrayList<Field> fields = new ArrayList<>();
  private final HashMap<int[], Field> fieldCounter = new HashMap<>();
  private final ArrayList<Field> occupied = new ArrayList<>();
  private final ArrayList<Field> doubleMoves = new ArrayList<>();

  /**
   * @author Carina
   * @use creates the mapfield in the comitee decided designway
   */
  public synchronized void createMap() {
    floor = new Floors(currentFloor, holeAmount, doubleDrawFields, this);
    //Top left to mid right
    fields.clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(new int[]{x, y});
        fieldCounter.put(new int[]{x, y}, field);
        fields.add(field);
      }
    }
    //1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(new int[]{x, y});
        fieldCounter.put(new int[]{x, y}, field);
        fields.add(field);
      }
    }
    printMap();
  }

  /**
   * @author Carina
   * @use prints the map
   */
  public synchronized void printMap() {
    int row = 0;
    for (var field : fields) {
      if (field.getId()[1] != row) {
        System.out.println();
        row = field.getId()[1];
      }
      System.out.print("Field X: " + field.getId()[0] + ", Y " + field.getId()[1] + " ");
    }
  }

  public ArrayList<Field> getFields() {
    return fields;
  }

  public int getRadius() {
    return radius;
  }

  public Floors getFloor() {
    return floor;
  }

  public int getCurrentFloor() {
    return currentFloor;
  }

  public int getHoleAmount() {
    return holeAmount;
  }

  public int getDoubleDrawFields() {
    return doubleDrawFields;
  }

  public void setFloor(Floors floor) {
    this.floor = floor;
  }

  public void setCurrentFloor(int currentFloor) {
    this.currentFloor = currentFloor;
  }

  public void setHoleAmount(int holeAmount) {
    this.holeAmount = holeAmount;
  }

  public void setDoubleDrawFields(int doubleDrawFields) {
    this.doubleDrawFields = doubleDrawFields;
  }

  public int getMaxFloors() {
    return maxFloors;
  }

  public HashMap<int[], Field> getFieldCounter() {
    return fieldCounter;
  }


  public ArrayList<Field> getOccupied() {
    return occupied;
  }

  public ArrayList<Field> getDoubleMoves() {
    return doubleMoves;
  }
}
