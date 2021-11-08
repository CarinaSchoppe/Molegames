package de.thundergames.game.map;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

  private final int radius;
  private int floor;
  private final int maxFloors;

  public Map(final int radius, final int maxFloors) {
    this.radius = radius;
    this.maxFloors = maxFloors;
  }

  private final ArrayList<Field> fields = new ArrayList<>();
  private final HashMap<int[], Field> fieldCounter = new HashMap<>();
  private final ArrayList<Hole> holes = new ArrayList<>();
  private final ArrayList<Field> occupied = new ArrayList<>();
  private final ArrayList<Field> doubleMoves = new ArrayList<>();

  /**
   * @author Carina
   * @use creates the mapfield in the comitee decided designway
   */
  public synchronized void createMap() {
    //Top left to mid right
    fields.clear();
    for (int y = 0; y < radius; y++) {
      for (int x = 0; x < radius + y; x++) {
        Field field = new Field(new int[]{x, y});
        fieldCounter.put(new int[]{x, y}, field);
        fields.add(field);
      }
    }
    //1 under mid: left to bottom right
    for (int y = radius; y < radius * 2 - 1; y++) {
      for (int x = y - radius + 1; x < radius * 2 - 1; x++) {
        Field field = new Field(new int[]{x, y});
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
    for (Field field : fields) {
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

  public int getFloor() {
    return floor;
  }

  public int getMaxFloors() {
    return maxFloors;
  }

  public HashMap<int[], Field> getFieldCounter() {
    return fieldCounter;
  }

  public ArrayList<Hole> getHoles() {
    return holes;
  }

  public ArrayList<Field> getOccupied() {
    return occupied;
  }

  public ArrayList<Field> getDoubleMoves() {
    return doubleMoves;
  }
}
