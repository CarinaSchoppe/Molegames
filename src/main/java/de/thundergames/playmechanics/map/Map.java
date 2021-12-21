/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 13:57 by Carina Latest changes made by Carina on 21.12.21, 13:55 All contents of "Map" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Map {
  private final HashSet<Field> holes;
  private final HashSet<Field> drawAgainFields;
  private final int points;
  private final transient HashMap<List<Integer>, Field> fieldMap = new HashMap<>();
  private transient Game game;

  /**
   * @param holes
   * @param drawAgainFields
   * @param points
   * @param game
   * @author Carina
   * @use creates a new Map object with the given radius
   */
  public Map(
    @NotNull final Game game,
    @NotNull final HashSet<Field> holes,
    @NotNull final HashSet<Field> drawAgainFields,
    final int points) {
    this.holes = holes;
    this.drawAgainFields = drawAgainFields;
    this.points = points;
    this.game = game;
  }

  /**
   * @param holes
   * @param drawAgainFields
   * @param points
   * @author Carina
   * @use creates a new Map object with the given radius
   */
  public Map(
    @NotNull final HashSet<Field> holes,
    @NotNull final HashSet<Field> drawAgainFields,
    final int points) {
    this.holes = holes;
    this.drawAgainFields = drawAgainFields;
    this.points = points;
  }

  public void build(@NotNull final Game game) {
    createMap(game.getRadius());
  }

  public void build(@NotNull final GameState gameState) {
    createMap(gameState.getRadius());
    changeFieldParams(gameState);
  }

  /**
   * @param radius
   * @author Carina and Philipp
   * @use creates the mapfield in the comitee decided designway
   */
  public void createMap(final int radius) {
    // Top left to mid right
    fieldMap.clear();
    for (var y = 0; y <= radius; y++) {
      for (var x = 0; x <= radius + y; x++) {
        var field = new Field(x, y);
        field.setMap(this);
        fieldMap.put(java.util.List.of(x, y), field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = radius + 1; y <= radius * 2; y++) {
      for (var x = y - radius; x <= radius * 2; x++) {
        var field = new Field(x, y);
        field.setMap(this);
        fieldMap.put(java.util.List.of(x, y), field);
      }
    }
  }

  /**
   * @param gameState
   * @author Carina
   * @use sets the properties on the field if its occupied, a hole, a draw again field etc.
   */
  public void changeFieldParams(@NotNull final GameState gameState) {
    for (var field : gameState.getFloor().getHoles()) {
      if (getFieldMap().containsKey(List.of(field.getX(), field.getY())))
        getFieldMap().get(List.of(field.getX(), field.getY())).setHole(true);
    }
    for (var field : gameState.getFloor().getDrawAgainFields()) {
      if (getFieldMap().containsKey(List.of(field.getX(), field.getY())))
        getFieldMap().get(List.of(field.getX(), field.getY())).setDrawAgainField(true);
    }
    for (var mole : gameState.getPlacedMoles()) {
      if (getFieldMap().containsKey(List.of(mole.getField().getX(), mole.getField().getY()))) {
        getFieldMap().get(List.of(mole.getField().getX(), mole.getField().getY())).setMole(mole);
        getFieldMap()
          .get(List.of(mole.getField().getX(), mole.getField().getY()))
          .setOccupied(true);
      }
    }
  }

  /**
   * @author Carina
   * @use prints the map
   */
  public void printMap() {
    var fields =
      new ArrayList<>(fieldMap.values())
        .stream()
        .sorted(
          Comparator.comparing(de.thundergames.playmechanics.map.Field::getY)
            .thenComparing(de.thundergames.playmechanics.map.Field::getX))
        .collect(Collectors.toList());
    var row = 0;
    for (var field : fields) {
      if (field.getY() != row) {
        row = field.getY();
        System.out.println();
      }
      System.out.print(
        "Field X: "
          + field.getX()
          + ", Y: "
          + field.getY()
          + " occupied: "
          + field.isOccupied()
          + ", hole: "
          + field.isHole()
          + ", drawAgainField: "
          + field.isDrawAgainField()
          + "    ");
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
}
