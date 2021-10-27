package game.player;

import game.Colors;
import game.Floors;
import game.map.Field;

public class Mole {

  private Colors color;
  private Floors floor;
  private Field field;

  public Mole(Colors color, Floors floor, Field field) {
    this.color = color;
    this.floor = floor;
    this.field = field;
  }

  public Colors getColor() {
    return color;
  }

  public Floors getFloor() {
    return floor;
  }

  public Field getField() {
    return field;
  }
}
