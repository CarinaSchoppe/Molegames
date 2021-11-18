/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "AIUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Floors;
import de.thundergames.playmechanics.map.Map;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AIUtil {


  /**
   * @param ai
   * @param json the jsonString that will be converted into a Map
   * @return
   * @author Carina
   * @use returns the map object created on the base of the jsonString
   */
  public Map createMapFromJson(@NotNull final AI ai, @NotNull final JSONObject json) {
    var radius = json.getInt("radius");
    var map = new Map(radius);
    var floor = new Floors(0, map, 0);
    // Top left to mid right
    floor.getFields().clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(List.of(x, y));
        setFieldItems(json, floor, field);
        floor.getFieldMap().put(List.of(field.getX(), field.getY()), field);
        floor.getFields().add(field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        setFieldItems(json, floor, field);
        floor.getFieldMap().put(java.util.List.of(x, y), field);
        floor.getFields().add(field);
      }
    }
    map.setFloor(floor);
    ai.getMolePositions().clear();
    for (var field : map.getFloor().getFields()) {
      if (field.isOccupied()) {
        if (ai.getPlayerMolesOnField().contains(field.getMole()) || ai.getPlayerMolesInHoles().contains(field.getMole())) {
          ai.getMolePositions().put(field.getMole(), field.getId());
        }
      }
    }
    ai.setMap(map);
    changeMoleFieldTypes(ai);
    return map;
  }


  /**
   * @param ai
   * @author Carina
   * @use changes the the lists for moles in the hole and the moles on the field
   */
  public void changeMoleFieldTypes(@NotNull final AI ai) {
    for (var field : ai.getMap().getFloor().getFields()) {
      if (field.isOccupied()) {
        if (ai.getPlayerMolesOnField().contains(field.getMole())) {
          if (field.isHole()) {
            ai.getPlayerMolesOnField().remove(field.getMole());
            ai.getPlayerMolesInHoles().add(field.getMole());
          } else {
            ai.getPlayerMolesInHoles().remove(field.getMole());
            ai.getPlayerMolesOnField().add(field.getMole());
          }
        }
      }
    }
  }

  /**
   * @param json  the jsonString of a map
   * @param floor the floor of the map with all that fields
   * @param field the field itself
   * @author Carina
   * @use sets the items on the field if its occupied and when by what moleID, a hole, a double draw field etc.
   */
  private void setFieldItems(@NotNull final JSONObject json, @NotNull final Floors floor, @NotNull final Field field) {
    field.setOccupied(json.getBoolean("field["
        + field.getX()
        + ","
        + field.getY()
        + "].occupied"), json.getInt("field["
        + field.getX()
        + ","
        + field.getY()
        + "].mole"));

    field.setDoubleMove(json.getBoolean("field["
        + field.getX()
        + ","
        + field.getY()
        + "].doubleMove"));
    field.setHole(json.getBoolean("field["
        + field.getX()
        + ","
        + field.getY()
        + "].hole"));
    field.setFloor(floor);
  }

  /**
   * @param ai
   * @param field  that will be checked with the mole
   * @param moleID that will be checked with the field
   * @return the distance between the mole and the field in form of X and Y
   * @author Carina
   * @use input the parameter and than returns the distance between the mole and the field
   */
  public List<Integer> getDistance(@NotNull final AI ai, @NotNull final Field field, final int moleID) {
    var mole = ai.getMap().getFloor().getFieldMap().get(ai.getMolePositions().get(moleID));
    var x = mole.getId().get(0);
    var y = mole.getId().get(1);
    var distanceX = Math.abs(field.getId().get(0) - x);
    var distanceY = Math.abs(field.getId().get(1) - y);
    return List.of(distanceX, distanceY);
  }

}
