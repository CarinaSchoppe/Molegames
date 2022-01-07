package de.thundergames.gameplay.player.board;

import java.util.Timer;
import java.util.TimerTask;

public class BoardCountDown {

  private long remainingTime;
  private Boolean countTimer;

  public void setRemainingTime(long remainingTime)
  {
    this.remainingTime = remainingTime;
  }

  public void setTimer(boolean count)
  {
    this.countTimer = count;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        if (remainingTime > 0) {
          GameBoard.getObserver().updateTime(remainingTime);
          if (countTimer) remainingTime = remainingTime - 1000;
        }
      }
    };
    timer.schedule(task, 0, 1000);
  }

  public void stopTimer() {
    countTimer=false;
  }

  public void continueTimer() {
    countTimer=true;
  }
}