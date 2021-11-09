package de.thundergames.gameplay.ai;

import de.thundergames.gameplay.player.PlayerHandler;
import org.jetbrains.annotations.NotNull;

public class AI extends PlayerHandler implements Runnable {
  private final Thread AIthread = new Thread(this);
  private boolean isMove = false;
  private final int port;
  private final String ip;

  public AI(@NotNull final String ip, final int port) {
    this.ip = ip;
    this.port = port;
  }

  public void start() {
    AIthread.start();
    isMove = true;
  }

  private void makeMove() {
  }

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
}
