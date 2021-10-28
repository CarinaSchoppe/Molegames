package gameplay.player;

public enum PlayerStates {

    WAIT(0),
    PICK(1),
    DRAW(2);

    private final int id;

    PlayerStates(int id) {
        this.id = id;
    }

    private PlayerStates currentState;

    public PlayerStates getCurrentState() {
        return currentState;
    }

    public void nextState() {
        currentState = currentState.id == 2 ? PlayerStates.WAIT : getByID(currentState.getId() + 1);
    }

    public int getId() {
        return id;
    }

    public PlayerStates getByID(int id) {
        switch (id) {
            case 0:
                return PlayerStates.WAIT;
            case 1:
                return PlayerStates.PICK;
            case 2:
                return PlayerStates.DRAW;
            case 3:
                return PlayerStates.WAIT;
        }
        return null;
    }
}
