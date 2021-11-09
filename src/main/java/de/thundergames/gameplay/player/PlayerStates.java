package de.thundergames.gameplay.player;

public enum PlayerStates {

  WAIT(0),
  PICK(1),
  DRAW(2);

  private final int id;

  PlayerStates(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
