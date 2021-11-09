package de.thundergames.game.util;

import de.thundergames.game.map.Map;
import org.json.JSONObject;

public class Settings {
  private int[] cards = new int[]{1, 1, 2, 2, 3, 3, 4, 4};
  private int timeToThink = 20;
  private boolean randomDraw = false;
  private Punishments punishment = Punishments.NOTHING;
  private int maxPlayers = 4;
  private int moleAmount = 4;
  private int maxFloors = 4;
  private final Game game;
  private int radius = 4;

  public Settings(Game game) {
    this.game = game;
  }

  public synchronized void updateConfiuration(JSONObject object) {
    if (object.get("randomDraw") != null)
      randomDraw = object.getBoolean("randomDraw");
    if (object.get("timeToThink") != null)
      timeToThink = object.getInt("timeToThink");
    if (object.get("punishment") != null)
      punishment = Punishments.valueOf(object.getString("punishment"));
    if (object.get("maxPlayers") != null)
      maxPlayers = object.getInt("maxPlayers");
    if (object.get("moleAmount") != null)
      moleAmount = object.getInt("moleAmount");
    if (object.get("maxFloors") != null)
      maxFloors = object.getInt("maxFloors");
    if (object.get("radius") != null) {
      radius = object.getInt("radius");
      game.setMap(new Map(radius, game));
    }
    if (object.get("cards") != null) {
      cards = new int[object.getJSONArray("cards").length()];
      for (int i = 0; i < cards.length; i++) {
        cards[i] = object.getJSONArray("cards").getInt(i);
      }
    }
  }

  public int getMaxFloors() {
    return maxFloors;
  }

  public int getRadius() {
    return radius;
  }
}
