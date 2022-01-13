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

import java.util.Timer;
import java.util.TimerTask;

public class BoardCountDown {

  private long remainingTime;
  private boolean showCount;
  private boolean stopCountAfterTurn;

  public void setRemainingTime(long remainingTime) {
    this.remainingTime = remainingTime;
    this.stopCountAfterTurn = false;
  }

  public void setTimer(boolean run) {
    this.showCount = run;
    var timer = new Timer();
    var task = new TimerTask() {
      @Override
      public void run() {
        if (!showCount) {
          GameBoard.getObserver().updateTime(remainingTime, false);
        } else if (remainingTime > 0) {
          GameBoard.getObserver().updateTime(remainingTime, true);
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
    stopCountAfterTurn = true;
  }

  public void continueTimer() {
    showCount = true;
  }

  public void checkForStopTimer() {
    if (stopCountAfterTurn) {
      stopCountAfterTurn = false;
      showCount = false;
    }
  }
}
