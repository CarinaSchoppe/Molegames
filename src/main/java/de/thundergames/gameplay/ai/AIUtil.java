package de.thundergames.gameplay.ai;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Floors;
import de.thundergames.playmechanics.map.Map;
import java.util.List;
import org.json.JSONObject;

public class AIUtil {

  public Map createMapFromJson(AI ai, JSONObject json) {
    var radius = json.getInt("radius");
    var map = new Map(radius);
    var floor = new Floors(0, map, 0);
    // Top left to mid right
    floor.getFields().clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(List.of(x, y));
        setFieldItems(json, floor, y, x, field);
        floor.getFieldMap().put(List.of(x, y), field);
        floor.getFields().add(field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        setFieldItems(json, floor, y, x, field);
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

  public void changeMoleFieldTypes(AI ai) {
    for (var field : ai.getMap().getFloor().getFields()) {
      if (field.isOccupied()) {
        if (field.isHole()) {
          if (ai.getPlayerMolesOnField().contains(field.getMole())) {
            ai.getPlayerMolesOnField().remove(field.getMole());
            ai.getPlayerMolesInHoles().add(field.getMole());
          }
        } else {
          if (ai.getPlayerMolesInHoles().contains(field.getMole())) {
            ai.getPlayerMolesInHoles().remove(field.getMole());
            ai.getPlayerMolesOnField().add(field.getMole());
          }
        }
      }
    }
  }

  private void setFieldItems(JSONObject json, Floors floor, int y, int x, Field field) {
    field.setOccupied(json.getBoolean("field["
        + x
        + ","
        + y
        + "].occupied"), json.getInt("field["
        + x
        + ","
        + y
        + "].mole"));

    field.setDoubleMove(json.getBoolean("field["
        + x
        + ","
        + y
        + "].doubleMove"));
    field.setHole(json.getBoolean("field["
        + field.getId().get(0)
        + ","
        + field.getId().get(1)
        + "].hole"));
    field.setFloor(floor);
  }
}
