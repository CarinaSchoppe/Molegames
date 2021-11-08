package de.thundergames.game.util;

import de.thundergames.game.map.Field;
import de.thundergames.game.map.Floors;
import org.jetbrains.annotations.NotNull;

public class Mole {

  private final Floors floor;
  private final Field field;
  private boolean inHole = false;

  public Mole(@NotNull final Floors floor, @NotNull final Field field) {
    this.floor = floor;
    this.field = field;
  }

  public Floors getFloor() {
    return floor;
  }

  public Field getField() {
    return field;
  }

  public boolean isMoveable() {
    //TODO hier logik einbauen
    return false;
  }

  public boolean isInHole() {
    return inHole;
  }

  public void setInHole(boolean inHole) {
    this.inHole = inHole;
  }
}


