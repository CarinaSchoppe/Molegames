package de.thundergames.gameplay.player;

import de.thundergames.game.util.Mole;

import java.util.ArrayList;

public class PlayerHandler {

  private final ArrayList<Mole> playerMoles = new ArrayList<>();
  private final ArrayList<Mole> playerMolesInHoles = new ArrayList<>();
  private final ArrayList<Mole> playerMolesOnField = new ArrayList<>();

  /**
   * @author Carina
   * @use gets the shutdown logic of a client
   */
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
