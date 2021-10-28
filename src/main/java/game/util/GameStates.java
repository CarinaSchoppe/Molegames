package game.util;

public enum GameStates {


    LOBBY(0),
    PREGAME(1),
    INGAME(2),
    WINNINGSTATE(3),
    RESETSTATE(4);

    int id;


    GameStates(int id) {
        this.id = id;
    }
}
