/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:21 by Carina Latest changes made by Carina on 09.01.22, 21:21 All contents of "GameLogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
   * @use add the parameters, and it will return if the move was valid with true or invalid with
   * false
   * @premisse the startpoint and endpoint must be in the playing field and the player was allowed to
   * move.
   */
  public static synchronized boolean wasLegalMove(
    final int[] start, final int[] stop, final int moveCounter, @NotNull final Map map) {
    if (map.getFieldMap().containsKey(List.of(start[0], start[1]))
      && map.getFieldMap().containsKey(List.of(stop[0], stop[1]))) {
      if (List.of(stop) != List.of(start) && moveCounter > 0) {
        if (stop[0] - start[0] == 0 && Math.abs(stop[1] - start[1]) == moveCounter //diagonal down left or top right
          || start[1] - stop[1] == 0 && Math.abs(stop[0] - start[0]) == moveCounter //left or right
          || stop[0] - start[0] == moveCounter
          && stop[1] - start[1] == moveCounter ||
          start[0] - stop[0] == moveCounter
            && start[1] - stop[1] == moveCounter) {
          if (map.getFieldMap().get(List.of(stop[0], stop[1])).isOccupied()) {
            System.out.println("Field is occupied!");
            return false;
          }
          //all straight movements
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
            //all diagonal movements
          } else if (Math.abs(stop[0] - start[0]) == Math.abs(stop[1] - start[1]) && Math.abs(stop[0] - start[0]) == moveCounter) {
            for (var i = 1; i < moveCounter; i++) {
              if (stop[0] - start[0] > 0 && stop[1] - start[1] > 0) {
                var field = map.getFieldMap().get(List.of(start[0] + i, start[1] + i));
                if (field.isOccupied()) {
                  if (field.getX() == start[0] + i && field.getY() == start[1] + i) {
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
        } else {
          System.out.println("Not doing a possible move!");
          return false;
        }
      } else {
        System.out.println(
          "Not doing a move. Start  = Stop: ["
            + start[0]
            + "|"
            + start[1]
            + "] ["
            + stop[0]
            + "|"
            + stop[1]
            + "]"
            + "  moveCounter: "
            + moveCounter
            + " start-stop: "
            + (stop[0] - start[0])
            + "|"
            + (stop[1] - start[1]));
        return false;
      }
    } else {
      System.out.println("Field start: [" + start[0] + "|" + start[1] + "] or field: [" + stop[0] + "|" + stop[1] + "] does not exist!");
      return false;
    }
  }

  /**
   * @param game
   * @author Carina
   * @use checks if a player has won when the player is the only one in a single hole floor
   */
  public void checkWinning(@NotNull final Game game) {
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
   * @use handles the player and the game when won
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
  public void performPunishment(@NotNull final Player player, @NotNull final Punishments reason) {
    var punishment = player.getGame().getSettings().getPunishment();
    if (MoleGames.getMoleGames().getServer().isDebug()) {
      System.out.println(
        "Performing punishment: "
          + punishment.getName()
          + " for the player: "
          + player.getName()
          + " and the reason: "
          + reason.getName());
    }
    MoleGames.getMoleGames()
      .getServer()
      .sendToAllGameClients(
        player.getGame(),
        MoleGames.getMoleGames()
          .getServer()
          .getPacketHandler()
          .movePenaltyNotification(
            player, player.getGame().getDeductedPoints(), punishment, reason.getName()));
    if (punishment == Punishments.POINTS) {
      player
        .getGame()
        .getScore()
        .getPoints()
        .put(
          player.getServerClient().getThreadID(),
          player.getGame().getScore().getPoints().get(player.getServerClient().getThreadID())
            - player.getGame().getDeductedPoints());
    } else if (punishment == Punishments.KICK) {
      MoleGames.getMoleGames().getServer().getPacketHandler().playerKickedPacket(player, player.getGame());
      player.getGame().removePlayerFromGame(player);
    }
  }
}
