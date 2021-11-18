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
  private final HashMap<Integer, Integer> pointsPerFloorForDoubleDraw = new HashMap<>();
  private int timeToThink = 5;
  private boolean randomDraw = false;
  private Punishments punishment = Punishments.NOTHING;
  private int maxPlayers = 4;
  private int moleAmount = 4;
  private int maxFloors = 4;
  private int radius = 4;

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
    if (!packet.isNull("randomDraw")) {
      randomDraw = packet.getBoolean("randomDraw");
    }
    if (!packet.isNull("thinkTime")) {
      timeToThink = packet.getInt("thinkTime");
    }
    if (!packet.isNull("punishment")) {
      punishment = Punishments.getByID(packet.getInt("punishment"));
    }
    if (!packet.isNull("maxPlayers")) {
      maxPlayers = packet.getInt("maxPlayers");
    }
    if (!packet.isNull("moleAmount")) {
      moleAmount = packet.getInt("moleAmount");
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
    if (!packet.isNull("pointsPerFloorForDoubleDraw")) {
      pointsPerFloorForDoubleDraw.clear();
      var map = packet.getJSONObject("pointsPerFloorForDoubleDraw").toMap();
      for (var entry : map.keySet()) {
        pointsPerFloorForDoubleDraw.put(
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
  public JSONObject toJsonConfiguration() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("randomDraw", randomDraw);
    jsonObject.put("timeToThink", timeToThink);
    jsonObject.put("punishment", punishment.getID());
    jsonObject.put("maxPlayers", maxPlayers);
    jsonObject.put("moleAmount", moleAmount);
    jsonObject.put("maxFloors", maxFloors);
    jsonObject.put("radius", radius);
    jsonObject.put("cards", cards);
    jsonObject.put("pointsMoleFloor", pointsForMoleInHoleForFloor);
    jsonObject.put("pointsPerFloorForDoubleDraw", pointsPerFloorForDoubleDraw);
    return jsonObject;
  }

  public int getMoleAmount() {
    return moleAmount;
  }

  public int getMaxFloors() {
    return maxFloors;
  }

  public int getRadius() {
    return radius;
  }

  public boolean isRandomDraw() {
    return randomDraw;
  }

  public int getTimeToThink() {
    return timeToThink;
  }

  public ArrayList<Integer> getCards() {
    return cards;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public Punishments getPunishment() {
    return punishment;
  }
}
