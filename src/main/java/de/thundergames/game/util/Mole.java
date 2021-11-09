package de.thundergames.game.util;

import de.thundergames.game.map.Field;
import de.thundergames.game.map.Floors;

public class Mole {

  private Floors floor;
  private Field field;
  private boolean inHole = false;
  private final int MoleID;

  public Mole(int moleID) {
    MoleID = moleID;
  }

  public int getMoleID() {
    return MoleID;
  }

  public boolean isMoveable() {
    //TODO hier logik einbauen
    return false;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Field getField() {
    return field;
  }
}


