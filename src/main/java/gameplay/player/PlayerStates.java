package gameplay.player;

import org.jetbrains.annotations.NotNull;

public enum PlayerStates {

  WAIT(0),
  PICK(1),
  DRAW(2);

  private final int id;

  PlayerStates(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  /**
   * @author Carina
   * @param id the id that will get the matching PlayerState as a return
   * @return PlayerStates
   */
  public PlayerStates getByID(final int id) {
    switch (id) {
      case 0:
      case 3:
        return PlayerStates.WAIT;
      case 1:
        return PlayerStates.PICK;
      case 2:
        return PlayerStates.DRAW;
    }
    return null;
  }
}
