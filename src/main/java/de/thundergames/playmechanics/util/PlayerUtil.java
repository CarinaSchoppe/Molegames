/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 22:24 by Carina latest changes made by Carina on 06.12.21, 22:21
 * All contents of "PlayerUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.playmechanics.util;

import de.thundergames.MoleGames;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerUtil {

  private final Player player;

  public PlayerUtil(@NotNull final Player player) {
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
    player.setTimerIsRunning(true);
    player.setTimer(new Timer());
    player
      .getTimer()
      .schedule(
        new TimerTask() {
          @Override
          public void run() {
            if (!player.isHasMoved()) {
              MoleGames.getMoleGames()
                .getGameHandler()
                .getGameLogic()
                .performPunishment(player, player.getGame().getSettings().getPunishment());
              MoleGames.getMoleGames()
                .getServer()
                .sendToAllGameClients(
                  player.getGame(),
                  MoleGames.getMoleGames()
                    .getPacketHandler()
                    .movePenaltyNotification(
                      player,
                      player.getGame().getDeductedPoints(),
                      player.getGame().getSettings().getPunishment(),
                      Punishments.NOMOVE.getName()));
              player.setHasMoved(true);
              player.setTimerIsRunning(false);
              if (MoleGames.getMoleGames().getServer().isDebug())
                System.out.println(
                  "Client " + player.getServerClient().getClientName() + " ran out of time");
              player.getGame().getGameUtil().nextPlayer();
              player.getTimer().cancel();
            }
          }
        },
        player.getGame().getSettings().getTurnTime());
  }

  public void handleTurnAfterAction() {
    player.setHasMoved(true);
    if (player.isTimerIsRunning()) {
      player.getTimer().cancel();
      player.getGame().getGameUtil().nextPlayer();
    }
  }
}
