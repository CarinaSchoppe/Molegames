/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 10:32
 * All contents of "Settings" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import de.thundergames.filehandling.GameConfiguration;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.map.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class Settings {

  /*

  ausrichter kann spieler zu einem spiel zuweisen
  TODO: hier mehr adden! und dann auch implementieren
   */

  final GameConfiguration gameConfiguration;
  private final ArrayList<Integer> cards = new ArrayList<>(List.of(1, 3, 1, 2, 4, 3, 4, 3, 1, 2, 5, 3, 1, 3, 2));
  private final Game game;
  private final HashMap<Integer, Integer> pointsForMoleInHoleForFloor = new HashMap<>() {
  };
  private int numberOfMoles;
  private final HashMap<Integer, Integer> pointsPerFloorForDrawAgainFields = new HashMap<>();
  private int turnTime = 5;
  private boolean pullDiscsOrdered = false;
  private Punishments punishment = Punishments.NOTHING;
  private int maxFloors = 4;
  private int radius = 4;
  private int maxPlayerCount = 0;
  private final int nextHolePoints = 10; //TODO: mehr implementen! 1
  private final int visualizationTime = 20;

  public Settings(@NotNull final Game game) {
    this.game = game;
    this.gameConfiguration = new GameConfiguration();
  }

  /**
   * @param packet the jsonObject that will update the configuration send by the GameMasterClient
   * @author Carina
   * @use pass in the new configuration from the GameMasterClient and it will automaticly update every single setting that was included in the jsonObject
   * @use this method is called in the GameMasterClient to the Server
   * @use updates the map and the Game directly
   */
  public synchronized void updateConfiuration(@NotNull final JSONObject packet) {
    if (!packet.isNull("pullDiscsOrdered")) {
      pullDiscsOrdered = packet.getBoolean("pullDiscsOrdered");
    }
    if (!packet.isNull("thinkTime")) {
      turnTime = packet.getInt("thinkTime");
    }
    if (!packet.isNull("movePenalty")) {
      punishment = Punishments.getByName(packet.getString("movePenalty"));
    }
    if (!packet.isNull("maxPlayerCount")) {
      maxPlayerCount = packet.getInt("maxPlayerCount");
    }
    if (!packet.isNull("numberOfMoles")) {
      numberOfMoles = packet.getInt("numberOfMoles");
    }
    if (!packet.isNull("maxFloors")) {
      maxFloors = packet.getInt("maxFloors");
    }
    if (!packet.isNull("radius")) {
      radius = packet.getInt("radius");
      game.setMap(new Map(radius, game));
    }
    if (!packet.isNull("pointsMoleFloor")) {
      pointsForMoleInHoleForFloor.clear();
      var map = packet.getJSONObject("pointsMoleFloor").toMap();
      for (var entry : map.keySet()) {
        pointsForMoleInHoleForFloor.put(
            Integer.parseInt(entry), Integer.parseInt(map.get(entry).toString()));
      }
    }
    if (!packet.isNull("pointsPerDrawAgainFields")) {
      pointsPerFloorForDrawAgainFields.clear();
      var map = packet.getJSONObject("pointsPerDrawAgainFields").toMap();
      for (var entry : map.keySet()) {
        pointsPerFloorForDrawAgainFields.put(
            Integer.parseInt(entry), Integer.parseInt(map.get(entry).toString()));
      }
    }

    if (!packet.isNull("cards")) {
      cards.clear();
      for (int i = 0; i < packet.getJSONArray("cards").length(); i++) {
        cards.add(packet.getJSONArray("cards").getInt(i));
      }
    }
  }

  /**
   * @return the Settings in a JsonObject format
   * @author Carina
   * @use this method is called in the GameMasterClient to the Server to convert the Settings to a jsonObject to save that on the system
   */
  public String toJsonConfiguration() {
    var object = new JSONObject();
    var floorArray = new String[game.getMap().getFloors().size()];
    for (int i = 0; i < game.getMap().getFloors().size(); i++) {
      floorArray[i] = game.getMap().getFloors().get(i).toJsonObject() + "";
    }
    object.put("maxPlayerCount", maxPlayerCount);
    object.put("radius", radius);
    object.put("numberOfMoles", numberOfMoles);
    object.put("turnTime", turnTime);
    object.put("levels", floorArray);
    object.put("pullDiscsOrdered", pullDiscsOrdered);
    object.put("pullDiscs", cards.toArray());
    object.put("visualisationTime", visualizationTime);
    object.put("movePenalty", punishment.getName());
    return object.toString();
  }

  public int getNumberOfMoles() {
    return numberOfMoles;
  }

  public int getMaxFloors() {
    return maxFloors;
  }

  public int getRadius() {
    return radius;
  }

  public boolean isPullDiscsOrdered() {
    return pullDiscsOrdered;
  }

  public int getTurnTime() {
    return turnTime;
  }

  public ArrayList<Integer> getCards() {
    return cards;
  }

  public int getMaxPlayers() {
    return maxPlayerCount;
  }

  public int getNextHolePoints() {
    return nextHolePoints;
  }

  public Punishments getPunishment() {
    return punishment;
  }
}
