/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 22:24 by Carina latest changes made by Carina on 06.12.21, 22:21
 * All contents of "GameLogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Punishments;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameLogic {

  /**
   * @param start       the startpoint in form of x and y
   * @param stop        the endpoint where the player wants to go in form of x and y
   * @param moveCounter the amounts of fields the player can move
   * @param map         of the de.thundergames.game and of the player
   * @return if the move is valid it will return true
   * @author Carina
   * @use add the parameters and it will return if the move was valid with true or invalid with
   * false
   * @premisse the startpoint and endpoint must be in the playingfield and the player was allowed to
   * move.
   */
  public static synchronized boolean wasLegalMove(
    @NotNull final int[] start,
    @NotNull final int[] stop,
    final int moveCounter,
    @NotNull final Map map) {
    if (map.getFieldMap().containsKey(List.of(start[0], start[1]))
      && map.getFieldMap().containsKey(List.of(stop[0], stop[1]))
      && start != stop) {
      if (stop[0] - start[0] == 0 && Math.abs(stop[1] - start[1]) == moveCounter
        || start[1] - stop[1] == 0 && Math.abs(stop[0] - start[0]) == moveCounter
        || Math.abs(stop[0] - start[0]) == Math.abs(stop[1] - start[1])
        && Math.abs(start[1] - stop[1]) == moveCounter) {
        if (map.getFieldMap().get(List.of(stop[0], stop[1])).isOccupied()) {
          return false;
        }
        if (stop[0] - start[0] == 0) {
          for (var i = 1; i < moveCounter; i++) {
            if (stop[1] - start[1] > 0) {
              var field = map.getFieldMap().get(List.of(stop[0], start[1] + i));
              if (field.isOccupied()) {
                if (field.getY() == start[1] + i && field.getX() == start[0]) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            } else if (stop[1] - start[1] < 0) {
              var field = map.getFieldMap().get(List.of(start[0], start[1] - i));
              if (field.isOccupied()) {
                if (field.getY() == start[1] - i && field.getX() == start[0]) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            }
          }
        } else if (stop[1] - start[1] == 0) {
          for (var i = 1; i < moveCounter; i++) {
            if (stop[0] - start[0] > 0) {
              var field = map.getFieldMap().get(List.of(start[0] + i, start[1]));
              if (field.isOccupied()) {
                if (field.getX() == start[0] + i && field.getY() == start[1]) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            } else if (stop[0] - start[0] < 0) {
              var field = map.getFieldMap().get(List.of(start[0] - i, start[1]));
              if (field.isOccupied()) {
                if (field.getX() == start[0] - i && field.getY() == start[1]) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            }
          }
        } else if (Math.abs(stop[0] - start[0]) == Math.abs(stop[1] - start[1])) {
          for (var i = 1; i < moveCounter; i++) {
            if (stop[0] - start[0] > 0 && stop[1] - start[1] > 0) {
              var field = map.getFieldMap().get(List.of(start[0] + i, start[1] + i));
              if (field.isOccupied()) {
                if (field.getX() == start[0] + i && field.getY() == start[1] + i) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            } else if (stop[0] - start[0] < 0 && stop[1] - start[1] > 0) {
              var field = map.getFieldMap().get(List.of(start[0] - i, start[1] + i));
              if (field.isOccupied()) {
                if (field.getX() == start[0] - i && field.getY() == start[1] + i) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            } else if (stop[0] - start[0] > 0 && stop[1] - start[1] < 0) {
              var field = map.getFieldMap().get(List.of(start[0] + i, start[1] - i));
              if (field.isOccupied()) {
                if (field.getX() == start[0] + i && field.getY() == start[1] - i) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            } else if (stop[0] - start[0] < 0 && stop[1] - start[1] < 0) {
              var field = map.getFieldMap().get(List.of(start[0] - i, start[1] - i));
              if (field.isOccupied()) {
                if (field.getX() == start[0] - i && field.getY() == start[1] - i) {
                  System.out.println("occupied field: " + field.getX() + " " + field.getY());
                  return false;
                }
              }
            }
          }
        }
        return true;
      }
    }
    return false;
  }

  /**
   * @param game
   * @author Carina
   * @use checks if a player has won when the player is the only one in a single hole foor
   */
  public synchronized void checkWinning(@NotNull final Game game) {
    var hole = 0;
    for (var field : game.getMap().getFieldMap().values()) {
      if (field.isHole()) {
        hole++;
      }
    }
    if (hole == 1) {
      win(game);
    }
  }

  /**
   * @param game
   * @author Carina
   * @use handles the player and the game when won TODO: handle win display the end
   */
  public void win(@NotNull final Game game) {
    game.endGame();
  }

  /**
   * @param player
   * @author Carina
   * @use handles the punishment / performs it to the player doing an invalid move punishments
   * performen
   */
  public synchronized void performPunishment(Player player, Punishments reason) {
    if (player.getGame().getSettings().getPunishment().equals(Punishments.POINTS)) {
      player
        .getGame()
        .getScore()
        .getPoints()
        .put(
          player.getClientID(),
          player.getGame().getScore().getPoints().get(player.getClientID())
            - player.getGame().getDeductedPoints());
    } else if (player.getGame().getSettings().getPunishment().equals(Punishments.KICK)) {
      player.getGame().removePlayerFromGame(player);
    }
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
            reason.getName()));
  }
}
