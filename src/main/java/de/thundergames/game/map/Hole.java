package de.thundergames.game.map;

import java.util.List;

public class Hole {
  final List<Integer> id;
  boolean used;

  public Hole(final List<Integer> id, final boolean used) {
    this.id = id;
    this.used = used;
  }
}
