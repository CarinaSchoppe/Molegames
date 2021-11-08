package game.map;

import org.jetbrains.annotations.NotNull;

public class Hole {
  final int[] id;
  boolean used;

  public Hole(final int[] id, final boolean used) {
    this.id = id;
    this.used = used;
  }

  public void setUsed(boolean used) {
    this.used = used;
  }
}
