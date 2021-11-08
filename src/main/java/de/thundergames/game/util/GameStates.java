package de.thundergames.game.util;

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

  public GameStates getCurrentState() {
    return currentState;
  }

  private GameStates currentState;

  GameStates(final int id) {
    this.id = id;
  }

  public void nextState() {
    currentState = currentState.id == 4 ? GameStates.LOBBY : getByID(currentState.getId() + 1);
  }

  public int getId() {
    return id;
  }

  public GameStates getByID(int id) {
    switch (id) {
      case 0:
      case 5:
        return GameStates.LOBBY;
      case 1:
        return GameStates.PREGAME;
      case 2:
        return GameStates.INGAME;
      case 3:
        return GameStates.WINNINGSTATE;
      case 4:
        return GameStates.RESETSTATE;
    }
    return null;
  }
}
