package de.thundergames.game.map;

import de.thundergames.game.util.Game;
import org.jetbrains.annotations.NotNull;

public class Map {

  private final int radius;

  private Floors floor;
  private int currentFloor;
  private int holeAmount; //TODO: here
  private int doubleDrawFields;

  public Map(final int radius, @NotNull final Game game) {
    this.radius = radius + 1;
    currentFloor = game.getSettings().getMaxFloors();
    createMap();
  }

  /**
   * @author Carina
   * @use creates the mapfield in the comitee decided designway
   */
  public synchronized void createMap() {
    floor = new Floors(currentFloor, holeAmount, doubleDrawFields, this);
    //Top left to mid right
    floor.getFields().clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(java.util.List.of(x, y));
        floor.getFieldMap().put(java.util.List.of(x, y), field);
        floor.getFields().add(field);
      }
    }
    //1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        floor.getFieldMap().put(java.util.List.of(x, y), field);
        floor.getFields().add(field);
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
    for (var field : floor.getFields()) {
      if (field.getId().get(1) != row) {
        System.out.println();
        row = field.getId().get(1);
      }
      System.out.print("Field X:" + field.getId().get(0) + ", Y:" + field.getId().get(1) + " occupied:" + field.isOccupied() + "   ");
    }
    System.out.println();
  }

  public Floors getFloor() {
    return floor;
  }
}
