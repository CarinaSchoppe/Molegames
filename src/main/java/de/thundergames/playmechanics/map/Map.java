/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 21:41 by Carina latest changes made by Carina on 22.11.21, 19:55 All contents of "Map" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

import de.thundergames.networking.util.interfaceItems.NetworkFloor;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Map extends NetworkFloor {

  private transient final HashMap<List<Integer>, Field> fieldMap = new HashMap<>();
  private transient Game game;


  /**
   * @param game the game the map is related to
   * @author Carina
   * @use creates a new Map object with the given radius
   */
  public Map(@NotNull final Game game) {
    this.game = game;
    createMap(game.getRadius());
  }

  /**
   * @param gameState the gameState the map is related to to the client
   * @author Carina
   * @use creates a new Map object with the given radius
   */
  public Map(GameState gameState) {
    createMap(gameState.getRadius());
  }

  /**
   * @param radius
   * @author Carina
   * @use creates the mapfield in the comitee decided designway
   */
  public synchronized void createMap(final int radius) {
    // Top left to mid right
    fieldMap.clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(List.of(x, y));
        field.setMap(this);
        fieldMap.put(java.util.List.of(x, y), field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        field.setMap(this);
        fieldMap.put(java.util.List.of(x, y), field);
      }
    }
    // printMap();
  }

  /**
   * @param gameState
   * @author Carina
   * @use sets the properties on the field if its occupied, a hole, a draw again field etc.
   */
  public void changeFieldParams(GameState gameState) {
    for (var field : gameState.getFloor().getHoles()) {
      getFieldMap().get(List.of(field.getX(), field.getY())).setHole(true);
    }
    for (var field : gameState.getFloor().getDrawAgainFields()) {
      getFieldMap().get(List.of(field.getX(), field.getY())).setDrawAgainField(true);
    }
    for (var mole : gameState.getPlacedMoles()) {
      getFieldMap().get(List.of(mole.getNetworkField().getX(), mole.getNetworkField().getY())).setMole(mole);
      getFieldMap().get(List.of(mole.getNetworkField().getX(), mole.getNetworkField().getY())).setOccupied(true);

    }
  }

  /**
   * @author Carina
   * @use prints the map
   */
  public synchronized void printMap() {
    int row = 0;

    for (var field : fieldMap.values()) {
      if (field.getY() != row) {
        System.out.println();
        row = field.getY();
      }
      System.out.print(
          "Field X: "
              + field.getX()
              + ", Y: "
              + field.getY()
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

  public HashMap<List<Integer>, Field> getFieldMap() {
    return fieldMap;
  }

  public Game getGame() {
    return game;
  }


}
