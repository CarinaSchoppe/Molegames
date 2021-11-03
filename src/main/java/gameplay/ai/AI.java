package gameplay.ai;

import game.util.Mole;
import gameplay.player.PlayerHandler;

public class AI extends PlayerHandler implements Runnable {
  private boolean shouldRun;
  private final Thread AIthread = new Thread(this);
  private boolean isMove = false;
  private final int port;
  private final String ip;

  public AI(boolean shouldRun, String ip, int port) {
    this.shouldRun = shouldRun;
    this.ip= ip;
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
        for (Mole mole : getPlayerMolesOnField()) {
          if (mole.isMoveable() && !moveable) {
            moveable = true;
            makeMove();
          }
        }
        if (!moveable) {
          for (Mole mole : getPlayerMolesInHoles()) {
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

  public boolean isShouldRun() {
    return shouldRun;
  }

  public void setShouldRun(boolean shouldRun) {
    this.shouldRun = shouldRun;
  }

  public Thread getAIThread() {
    return AIthread;
  }
}
