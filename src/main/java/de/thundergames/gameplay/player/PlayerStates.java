package de.thundergames.gameplay.player;

public enum PlayerStates {

  JOIN(3),
  WAIT(0),
  MOVE(1),
  DRAW(2);

  private final int id;

  PlayerStates(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
