package de.thundergames.game.map;

import de.thundergames.game.util.Game;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {

  private final int radius;

  private Floors floor;
  private final Game game;
  private int currentFloor;
  private int holeAmount;
  private int doubleDrawFields;

  public Map(final int radius, @NotNull final Game game) {
    this.game = game;
    this.radius = radius + 1;
    currentFloor = game.getSettings().getMaxFloors();
    createMap();
  }

  private final ArrayList<Field> fields = new ArrayList<>();
  private final HashMap<int[], Field> fieldMap = new HashMap<>();


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
        fieldMap.put(new int[]{x, y}, field);
        fields.add(field);
      }
    }
    //1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(new int[]{x, y});
        fieldMap.put(new int[]{x, y}, field);
        fields.add(field);
      }
    }
    // printMap();
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

  public Floors getFloor() {
    return floor;
  }
}
