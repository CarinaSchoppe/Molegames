package gameplay.player;

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

  public PlayerStates getByID(int id) {
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
