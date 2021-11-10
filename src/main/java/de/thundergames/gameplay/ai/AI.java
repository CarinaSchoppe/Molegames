package de.thundergames.gameplay.ai;

import de.thundergames.play.util.Mole;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AI implements Runnable {

  private final ArrayList<Mole> playerMolesInHoles = new ArrayList<>();
  private final ArrayList<Mole> playerMolesOnField = new ArrayList<>();
  private final Thread AIthread = new Thread(this);
  private boolean isMove = false;
  private final int port;
  private final String ip;
  private final int gameID;

  public AI(@NotNull final String ip, final int port, final int gameID) {
    this.ip = ip;
    this.port = port;
    this.gameID = gameID;
  }

  /**
   * @author Carina
   * @use is called after the constructor to initiate everything needed than starts the AI
   */
  public void start() {
    AIthread.start();
    isMove = true;
  }

  private void makeMove() {
  }

  /**
   * @author Carina
   * @use is called when an AI starts its job
   */
  @Override
  public void run() {
    boolean moveable = false;
    while (true) {
      if (isMove) {
        for (var mole : getPlayerMolesOnField()) {
          if (mole.isMoveable() && !moveable) {
            moveable = true;
            makeMove();
          }
        }
        if (!moveable) {
          for (var mole : getPlayerMolesInHoles()) {
            if (mole.isMoveable() && !moveable) {
              moveable = true;
              makeMove();
            }
          }
        }
        moveable = false;
        isMove = false;
      }
    }
  }

  public ArrayList<Mole> getPlayerMolesInHoles() {
    return playerMolesInHoles;
  }

  public ArrayList<Mole> getPlayerMolesOnField() {
    return playerMolesOnField;
  }
}
