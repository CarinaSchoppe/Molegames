package de.thundergames.gameplay.player.board;

import java.util.Timer;
import java.util.TimerTask;

public class BoardCountDown {

  private long remainingTime;
  private Boolean showCount;
  private Boolean stopCountAfterTurn;

  public void setRemainingTime(long remainingTime)
  {
    this.remainingTime = remainingTime;
    this.stopCountAfterTurn=false;
  }

  public void setTimer(boolean run)
  {
    this.showCount = run;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        if (!showCount) {
          GameBoard.getObserver().updateTime(remainingTime,false);
        }
        else if (remainingTime > 0)
        {
          GameBoard.getObserver().updateTime(remainingTime,true);
          remainingTime = remainingTime - 1000;
        }
      }
    };
    timer.schedule(task, 0, 1000);
  }

  public Boolean getShowCount() {
    return showCount;
  }

  public void stopCountAfterTurn() {
    stopCountAfterTurn=true;
  }

  public void continueTimer() {
    showCount=true;
  }

  public void checkForStopTimer() {
    if (stopCountAfterTurn)
    {
      stopCountAfterTurn=false;
      showCount=false;
    }
  }
}