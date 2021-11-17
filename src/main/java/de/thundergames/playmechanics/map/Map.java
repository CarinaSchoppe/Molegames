/*
 *
 *  *     / **
 *  *      *   Copyright Notice                                             *
 *  *      *   Copyright (c) SwtPra10 | ThunderGames 2021                         *
 *  *      *   Created: 05.05.2018 / 11:59                                  *
 *  *      *   All contents of this source text are protected by copyright. *
 *  *      *   The copyright law, unless expressly indicated otherwise, is  *
 *  *      *   at SwtPra10 | ThunderGames. All rights reserved                    *
 *  *      *   Any type of duplication, distribution, rental, sale, award,  *
 *  *      *   Public accessibility or other use                            *
 *  *      *   Requires the express written consent of SwtPra10 | ThunderGames.   *
 *  *      **
 *  *
 */
package de.thundergames.playmechanics.map;

import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.game.Game;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Map {

  private final int radius;
  private int currentFloor;
  private Floors floor;
  private int holeAmount; // TODO: here
  private int doubleDrawFields;
  private Game game;


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
    floor = new Floors(currentFloor, holeAmount, doubleDrawFields, this);
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
      if (field.getId().get(1) != row) {
        System.out.println();
        row = field.getId().get(1);
      }
      System.out.print(
          "Field X: "
              + field.getId().get(0)
              + ", Y: "
              + field.getId().get(1)
              + " occupied: "
              + field.isOccupied()
              + ", hole: " + field.isHole() + ", doubledraw: " + field.isDoubleMove() + "    "
      );
    }
    System.out.println();
  }


  public void sendMap(ServerThread client) {
    client.sendPacket(new Packet(new JSONObject().put("type", Packets.MAP.getPacketType()).put("values", toJSONString())));
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
              + field.getId().get(0)
              + ","
              + field.getId().get(1)
              + "].occupied",
          field.isOccupied());
      object.put(
          "field["
              + field.getId().get(0)
              + ","
              + field.getId().get(1)
              + "].hole",
          field.isHole());
      object.put(
          "field["
              + field.getId().get(0)
              + ","
              + field.getId().get(1)
              + "].doubleMove",
          field.isDoubleMove());
      if (field.isOccupied()) {
        object.put(
            "field["
                + field.getId().get(0)
                + ","
                + field.getId().get(1)
                + "].mole",
            field.getFloor().getMap().getGame().getMoleIDMap().get(field.getMole()).getMoleID());
      } else {
        object.put(
            "field["
                + field.getId().get(0)
                + ","
                + field.getId().get(1)
                + "].mole",
            field.getMole());
      }
    }
    return object.toString();
  }

  public Game getGame() {
    return game;
  }

  public Floors getFloor() {
    return floor;
  }

  public void setFloor(Floors floor) {
    this.floor = floor;
  }
}
