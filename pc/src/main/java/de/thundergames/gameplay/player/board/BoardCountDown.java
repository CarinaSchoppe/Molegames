/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 13.01.22, 17:45 by Carina Latest changes made by Carina on 13.01.22, 17:45 All contents of "BoardCountDown" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import lombok.Getter;
import lombok.Setter;

import java.util.Timer;
import java.util.TimerTask;

@Getter
@Setter
public class BoardCountDown {

  private long remainingTime;
  private boolean showCount;
  private boolean stopCountAfterTurn;
  private boolean stopTurnOver;
  private static TimerTask task;

  /**
   * @author Marc
   * @use set the remaining time
   */
  public void setRemainingTime(long remainingTime) {
    this.remainingTime = remainingTime;
    this.stopCountAfterTurn = false;
  }

  /**
   * @author Marc
   * @use set timer for remaining time of gameboard
   */
  public void setTimer(boolean run) {
    this.showCount = run;
    var timer = new Timer();
    task = new TimerTask() {
      @Override
      public void run() {
        if (!showCount) {
          GameBoard.getObserver().updateTime(remainingTime, false);
        } else if (remainingTime > 0 && !stopTurnOver) {
          GameBoard.getObserver().updateTime(remainingTime, true);
          remainingTime = remainingTime - 100;
        }
      }
    };
    timer.schedule(task, 0, 100);
  }

  /**
   * @author Marc
   * @use return value of count should be shown
   */
  public Boolean getShowCount() {
    return showCount;
  }


  /**
   * @author Marc
   * @use set value that counter should be stopped after turn end
   */
  public void stopCountAfterTurn() {
    stopCountAfterTurn = true;
  }

  /**
   * @author Marc
   * @use contunue timer for remaining time
   */
  public void continueTimer() {
    showCount = true;
  }

  /**
   * @author Marc
   * @use check if remaining time should be stopped
   */
  public void checkForStopTimer() {
    if (stopCountAfterTurn) {
      stopCountAfterTurn = false;
      showCount = false;
    }
  }

  /**
   * @author Marc
   * @use delete the timer
   */
  public void deleteTimer() {
    task.cancel();
  }
}
