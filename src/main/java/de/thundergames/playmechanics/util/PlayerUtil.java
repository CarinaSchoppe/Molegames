/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 13:45 by Carina latest changes made by Carina on 23.11.21, 13:45 All contents of "PlayerUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerUtil {

  private final Player player;

  public PlayerUtil(Player player) {
    this.player = player;
  }

  /**
   * @author Carina
   * @use will be called when a player has done a move and needs new cards cause all are empty
   * @see Settings
   */
  public void refillCards() {
    if (player.getCards().isEmpty()) {
      player.getCards().addAll(player.getGame().getSettings().getPullDiscs());
    }
  }

  /**
   * @author Carina
   * @use startes the time a player got for its move
   * @see Settings
   */
  public void startThinkTimer() {
    player.setStartRemainingTime(System.currentTimeMillis());
    player.setHasMoved(false);
    player.setTimer(new Timer());
    player.setTimerIsRunning(true);
    player.getTimer().schedule(
        new TimerTask() {
          @Override
          public void run() {
            player.setHasMoved(true);
            player.setTimerIsRunning(false);
            System.out.println("Client " + player.getServerClient().getConnectionID() + " ran out of time");
            player.getGame().getGameUtil().nextPlayer();
          }
        },
        player.getGame().getSettings().getTurnTime());
  }

  public void handleTurnAfterAction() {
    player.setHasMoved(true);
    if (player.isTimerIsRunning()) {
      player.getTimer().purge();
      player.getTimer().cancel();
      player.getGame().getGameUtil().nextPlayer();
    }
  }
}
