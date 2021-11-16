package de.thundergames.gameplay.ai;

public enum Directions {
  UP(0),
  DOWN(1),
  LEFT(2),
  RIGHT(3),
  UP_LEFT(4),
  UP_RIGHT(5),
  DOWN_LEFT(6),
  DOWN_RIGHT(7);

  private int value;

  Directions(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
