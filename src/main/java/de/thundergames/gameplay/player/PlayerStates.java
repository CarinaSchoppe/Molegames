package de.thundergames.gameplay.player;

/**
 * @author Carina
 * @use the PlayerStates a player can have
 * @use will be changed every time a player can do something
 * @see Player
 */
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
