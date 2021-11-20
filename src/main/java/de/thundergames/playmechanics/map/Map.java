/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 10:32
 * All contents of "Map" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.util.interfaceItems.NetworkFloor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Map extends NetworkFloor {

  private final Game game;
  private List<Field> fields;
  private final HashMap<List<Integer>, Field> fieldMap = new HashMap<>();
  private final List<Field> occupied = new ArrayList<>();


  /**
   * @param game the game the map is related to
   * @author Carina
   * @use creates a new Map object with the given radius
   */
  public Map(@NotNull final Game game) {
    this.game = game;
    createMap();
  }

  /**
   * @author Carina
   * @use creates the mapfield in the comitee decided designway
   */
  public synchronized void createMap() {
    // Top left to mid right
    fieldMap.clear();
    for (var y = 0; y < game.getRadius(); y++) {
      for (var x = 0; x < game.getRadius() + y; x++) {
        var field = new Field(List.of(x, y));
        field.setMap(this);
        fields.add(field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = game.getRadius(); y < game.getRadius() * 2 - 1; y++) {
      for (var x = y - game.getRadius() + 1; x < game.getRadius() * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        field.setMap(this);
        fieldMap.put(java.util.List.of(x, y), field);
        fields.add(field);
      }
    }
    //printMap();
  }

  /**
   * @author Carina
   * @use prints the map
   */
  public synchronized void printMap() {
    int row = 0;
    for (var field : fields) {
      if (field.getField().get(1) != row) {
        System.out.println();
        row = field.getField().get(1);
      }
      System.out.print(
          "Field X: "
              + field.getField().get(0)
              + ", Y: "
              + field.getField().get(1)
              + " occupied: "
              + field.isOccupied()
              + ", hole: " + field.isHole() + ", drawAgainField: " + field.isDrawAgainField() + "    "
      );
    }
    System.out.println();
  }

  /**
   * @param x the x position of the Field
   * @param y the y position of the Field
   * @return if the field does exist
   * @author Carina
   */
  public boolean existField(final int x, final int y) {
    return fieldMap.containsKey(List.of(x, y));
  }


  public List<Field> getFields() {
    return fields;
  }

  public HashMap<List<Integer>, Field> getFieldMap() {
    return fieldMap;
  }

  public Game getGame() {
    return game;
  }

  public List<Field> getOccupied() {
    return occupied;
  }
}
