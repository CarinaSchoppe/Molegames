package de.thundergames.game.util;

import de.thundergames.filehandling.GameConfiguration;
import de.thundergames.game.map.Map;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Settings {
  private final ArrayList<Integer> cards = new ArrayList<>(List.of(1, 1, 2, 2, 3, 3, 4, 4));
  private int timeToThink = 20;
  private boolean randomDraw = false;
  private Punishments punishment = Punishments.NOTHING;
  private int maxPlayers = 4;
  private int moleAmount = 4;
  private int maxFloors = 4;
  private final Game game;
  private int radius = 4;
  final GameConfiguration gameConfiguration;

  public Settings(Game game) {
    this.game = game;
    this.gameConfiguration = new GameConfiguration(toJsonConfiguration());
  }

  public synchronized void updateConfiuration(JSONObject packet) {
    if (!packet.isNull("randomDraw"))
      randomDraw = packet.getBoolean("randomDraw");
    if (!packet.isNull("timeToThink"))
      timeToThink = packet.getInt("timeToThink");
    if (!packet.isNull("punishment"))
      punishment = Punishments.getByID(packet.getInt("punishment"));
    if (!packet.isNull("maxPlayers"))
      maxPlayers = packet.getInt("maxPlayers");
    if (!packet.isNull("moleAmount"))
      moleAmount = packet.getInt("moleAmount");
    if (!packet.isNull("maxFloors"))
      maxFloors = packet.getInt("maxFloors");
    if (!packet.isNull("radius")) {
      radius = packet.getInt("radius");
      game.setMap(new Map(radius, game));
    }
    if (!packet.isNull("cards")) {
      cards.clear();
      for (int i = 0; i < packet.getJSONArray("cards").length(); i++) {
        cards.add(packet.getJSONArray("cards").getInt(i));
      }
    }
  }

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
