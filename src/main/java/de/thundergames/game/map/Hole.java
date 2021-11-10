package de.thundergames.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Hole {
  final List<Integer> id;
  boolean used;

  public Hole(@NotNull final  List<Integer> id, final boolean used) {
    this.id = id;
    this.used = used;
  }
}
