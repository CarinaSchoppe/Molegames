/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:35 by Carina Latest changes made by Carina on 09.01.22, 21:32 All contents of "BoardCountDown" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
  private Boolean countTimer;

  public void setRemainingTime(long remainingTime) {
    this.remainingTime = remainingTime;
  }

  public void setTimer(final boolean count) {
    this.countTimer = count;
    var timer = new Timer();
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
    countTimer = false;
  }

  public void continueTimer() {
    countTimer = true;
  }
}
