package game.map;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

  private int radius;
  private int floor;
  private int maxFloors = -1;

  public Map(int radius, int maxFloors) {
    this.radius = radius;
    this.maxFloors = maxFloors;
  }

  public Map(int radius) {
    this.radius = radius;
  }

  private ArrayList<Field> fields = new ArrayList();
  private HashMap<int[], Field> fieldCounter = new HashMap<>();

  private int id_x = 0;
  private int id_y = 0;

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

  public synchronized void printMap() {
    int row = 0;
    for (Field field : fields) {
      if (field.getId()[1] != row) {
        System.out.println();
        row = field.getId()[1];
      }
      System.out.print("Field X: " + field.getId()[0] + ", Y " + field.getId()[1] +" ");
    }
  }


  public ArrayList<Field> getFields() {
    return fields;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }
}
