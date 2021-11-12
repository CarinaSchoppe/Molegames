package de.thundergames.play.map;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Hole {
  final List<Integer> id;
  boolean used;

  public Hole(@NotNull final List<Integer> id, final boolean used) {
    this.id = id;
    this.used = used;
  }
}
