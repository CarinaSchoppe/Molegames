package gameplay.player;

import game.util.Mole;

import java.util.ArrayList;

public class PlayerHandler {

    private final ArrayList<Mole> playerMoles = new ArrayList<>();
    private final ArrayList<Mole> playerMolesInHoles = new ArrayList<>();
    private final ArrayList<Mole> playerMolesOnField = new ArrayList<>();

    public static void shutdown() {

    }



    public ArrayList<Mole> getPlayerMoles() {
        return playerMoles;
    }

    public ArrayList<Mole> getPlayerMolesInHoles() {
        return playerMolesInHoles;
    }

    public ArrayList<Mole> getPlayerMolesOnField() {
        return playerMolesOnField;
    }
}
