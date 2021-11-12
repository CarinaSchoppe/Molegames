package de.thundergames.play.game;

/**
 * @author Carina
 * @use the GameStats that a de.thundergames.game can have
 * @see Game as the class using the GameStates
 */
public enum GameStates {
  LOBBY(0),
  PREGAME(1),
  INGAME(2),
  WINNINGSTATE(3),
  RESETSTATE(4);

  private final int id;

  GameStates(final int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}
