package de.thundergames.game.util;

import de.thundergames.game.map.Map;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Settings {
  private ArrayList<Integer> cards = new ArrayList<>(List.of(1, 1, 2, 2, 3, 3, 4, 4));
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

  public synchronized void updateConfiuration(JSONObject packet) {
    if (packet.get("randomDraw") != null)
      randomDraw = packet.getBoolean("randomDraw");
    if (packet.get("timeToThink") != null)
      timeToThink = packet.getInt("timeToThink");
    if (packet.get("punishment") != null)
      punishment = Punishments.valueOf(packet.getString("punishment"));
    if (packet.get("maxPlayers") != null)
      maxPlayers = packet.getInt("maxPlayers");
    if (packet.get("moleAmount") != null)
      moleAmount = packet.getInt("moleAmount");
    if (packet.get("maxFloors") != null)
      maxFloors = packet.getInt("maxFloors");
    if (packet.get("radius") != null) {
      radius = packet.getInt("radius");
      game.setMap(new Map(radius, game));
    }
    if (packet.get("cards") != null) {
      cards.clear();
      for (int i = 0; i < packet.getJSONArray("cards").length(); i++) {
        cards.add(packet.getJSONArray("cards").getInt(i));
      }
    }
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
}
