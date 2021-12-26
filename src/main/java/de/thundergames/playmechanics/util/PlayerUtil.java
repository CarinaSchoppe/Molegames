/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
 * All contents of "PlayerUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import lombok.Data;

@Data
public class PlayerUtil {

  private final Player player;

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
  public synchronized void startThinkTimer() {
    player.setStartRemainingTime(System.currentTimeMillis());
    player.setTimerIsRunning(true);
    /*TODO:
    player.setTimer(new Timer());
    try {
      player
          .getTimer()
          .schedule(
              new TimerTask() {
                @Override
                public void run() {
                  if (MoleGames.getMoleGames().getServer().isDebug()) {
                    System.out.println(
                        "Client " + player.getServerClient().getThreadID() + " ran out of time");
                  }
                  MoleGames.getMoleGames()
                      .getGameHandler()
                      .getGameLogic()
                      .performPunishment(player, Punishments.NOMOVE);
                  player.setTimerIsRunning(false);
                  player.getGame().getGameUtil().nextPlayer();
                  player.getTimer().cancel();
                }
              },
              player.getGame().getSettings().getTurnTime());
    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }

  public void handleTurnAfterAction() {
    if (player.isTimerIsRunning()) {
      player.getTimer().cancel();
      player.getGame().getGameUtil().nextPlayer();
    }
  }
}
