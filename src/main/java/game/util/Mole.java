package game.util;

import game.map.Field;
import game.map.Floors;

public class Mole {

  private final Floors floor;
  private final Field field;
  private boolean inHole = false;

  public Mole( Floors floor, Field field) {
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


