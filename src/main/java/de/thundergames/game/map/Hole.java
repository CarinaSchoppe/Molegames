package de.thundergames.game.map;

public class Hole {
  final int[] id;
  boolean used;

  public Hole(final int[] id, final boolean used) {
    this.id = id;
    this.used = used;
  }

  public void setUsed(final boolean used) {
    this.used = used;
  }
}
