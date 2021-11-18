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

import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.game.Game;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Map {

  private final int radius;
  private int currentFloor;
  private Floor floor;
  private int holeAmount; // TODO: here
  private int drawAgainFields;
  private Game game;
  private final ArrayList<Floor> floors = new ArrayList<>(); //TODO: implement! 1


  /**
   * @param radius the radius of a map
   * @param game   the game the map is related to
   * @author Carina
   * @use creates a new Map object with the given radius
   */
  public Map(final int radius, @NotNull final Game game) {
    this.radius = radius + 1;
    this.game = game;
    currentFloor = game.getSettings().getMaxFloors();
    createMap();
  }

  public Map(int radius) {
    this.radius = radius;
  }


  /**
   * @author Carina
   * @use creates the mapfield in the comitee decided designway
   */
  public synchronized void createMap() {
    floor = new Floor(currentFloor, holeAmount, drawAgainFields, this);
    // Top left to mid right
    floor.getFields().clear();
    for (var y = 0; y < radius; y++) {
      for (var x = 0; x < radius + y; x++) {
        var field = new Field(List.of(x, y));
        field.setFloor(floor);
        floor.getFieldMap().put(List.of(x, y), field);
        floor.getFields().add(field);
      }
    }
    // 1 under mid: left to bottom right
    for (var y = radius; y < radius * 2 - 1; y++) {
      for (var x = y - radius + 1; x < radius * 2 - 1; x++) {
        var field = new Field(java.util.List.of(x, y));
        field.setFloor(floor);
        floor.getFieldMap().put(java.util.List.of(x, y), field);
        floor.getFields().add(field);
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
    for (var field : floor.getFields()) {
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
   * @param client
   * @author Carina
   * @use sends the map as a packet string to the client
   */
  public void sendMap(ServerThread client) {
    client.sendPacket(new Packet(new JSONObject().put("type", Packets.MAP.getPacketType()).put("value", toJSONString())));
  }

  /**
   * @param x the x position of the Field
   * @param y the y position of the Field
   * @return if the field does exist
   * @author Carina
   */
  public boolean existField(final int x, final int y) {
    return floor.getFieldMap().containsKey(List.of(x, y));
  }

  /**
   * @return the JSONSTRING of the map with all needed information
   * @author Carina
   * @use will be saved to the gameConfig stuff.
   */
  public synchronized String toJSONString() {
    JSONObject object = new JSONObject();
    object.put("radius", radius);
    object.put("fields", floor.getFields().size());
    for (var field : floor.getFields()) {
      object.put(
          "field["
              + field.getField().get(0)
              + ","
              + field.getField().get(1)
              + "].occupied",
          field.isOccupied());
      object.put(
          "field["
              + field.getField().get(0)
              + ","
              + field.getField().get(1)
              + "].hole",
          field.isHole());
      object.put(
          "field["
              + field.getField().get(0)
              + ","
              + field.getField().get(1)
              + "].drawAgainField",
          field.isDrawAgainField());
      if (field.isOccupied()) {
        object.put(
            "field["
                + field.getField().get(0)
                + ","
                + field.getField().get(1)
                + "].mole",
            field.getFloor().getMap().getGame().getMoleIDMap().get(field.getMole()).getMoleID());
      } else {
        object.put(
            "field["
                + field.getField().get(0)
                + ","
                + field.getField().get(1)
                + "].mole",
            field.getMole());
      }
    }
    return object.toString();
  }

  public Game getGame() {
    return game;
  }

  public Floor getFloor() {
    return floor;
  }

  public ArrayList<Floor> getFloors() {
    return floors;
  }

  public void setFloor(Floor floor) {
    this.floor = floor;
  }
}
