/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 17.01.22, 22:42 by Carina Latest changes made by Carina on 17.01.22, 22:39 All contents of "GameLogic" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.game;

import de.thundergames.MoleGames;
import de.thundergames.networking.server.Server;
import de.thundergames.networking.server.ServerThread;
import de.thundergames.networking.util.Network;
import de.thundergames.playmechanics.map.Directions;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Punishments;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static de.thundergames.playmechanics.map.Directions.RIGHT;

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
  public static synchronized boolean wasLegalMove(final Network network, final int[] start, final int[] stop, final int moveCounter, @NotNull final Map map) {
    if (map.getFieldMap().containsKey(List.of(start[0], start[1])) && map.getFieldMap().containsKey(List.of(stop[0], stop[1]))) {
      if (List.of(stop) != List.of(start) && moveCounter > 0) {
        if (stop[0] - start[0] == 0 && Math.abs(stop[1] - start[1]) == moveCounter //diagonal down left or top right
          || start[1] - stop[1] == 0 && Math.abs(stop[0] - start[0]) == moveCounter //left or right
          || stop[0] - start[0] == moveCounter && stop[1] - start[1] == moveCounter || start[0] - stop[0] == moveCounter && start[1] - stop[1] == moveCounter) {
          if (map.getFieldMap().get(List.of(stop[0], stop[1])).isOccupied()) {
            if (!(network instanceof Server)) {
              System.out.println("Client: End-Field is occupied! Field: [" + stop[0] + "|" + stop[1] + "]");
            }
            return false;
          }
          //all straight movements
          if (stop[0] - start[0] == 0) {
            for (var i = 1; i < moveCounter; i++) {
              if (stop[1] - start[1] > 0) {
                var field = map.getFieldMap().get(List.of(stop[0], start[1] + i));
                if (field.isOccupied()) {
                  if (field.getY() == start[1] + i && field.getX() == start[0]) {
                    if (!(network instanceof Server)) {
                      System.out.println("Client: Field a on the way is occupied! Field: [" + field.getX() + "|" + field.getY() + "]");
                    }
                    return false;
                  }
                }
              } else if (stop[1] - start[1] < 0) {
                var field = map.getFieldMap().get(List.of(start[0], start[1] - i));
                if (field.isOccupied()) {
                  if (field.getY() == start[1] - i && field.getX() == start[0]) {
                    if (!(network instanceof Server)) {
                      System.out.println("Client: Field a on the way is occupied! Field: [" + field.getX() + "|" + field.getY() + "]");
                    }
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
                    if (!(network instanceof Server)) {
                      System.out.println("Client: Field a on the way is occupied! Field: [" + field.getX() + "|" + field.getY() + "]");
                    }
                    return false;
                  }
                }
              } else if (stop[0] - start[0] < 0) {
                var field = map.getFieldMap().get(List.of(start[0] - i, start[1]));
                if (field.isOccupied()) {
                  if (field.getX() == start[0] - i && field.getY() == start[1]) {
                    if (!(network instanceof Server)) {
                      System.out.println("Client: Field a on the way is occupied! Field: [" + field.getX() + "|" + field.getY() + "]");
                    }
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
                    if (!(network instanceof Server)) {
                      System.out.println("Client: Field a on the way is occupied! Field: [" + field.getX() + "|" + field.getY() + "]");
                    }
                    return false;
                  }
                }
              } else if (stop[0] - start[0] < 0 && stop[1] - start[1] < 0) {
                var field = map.getFieldMap().get(List.of(start[0] - i, start[1] - i));
                if (field.isOccupied()) {
                  if (field.getX() == start[0] - i && field.getY() == start[1] - i) {
                    if (!(network instanceof Server)) {
                      System.out.println("Client: Field a on the way is occupied! Field: [" + field.getX() + "|" + field.getY() + "]");
                    }
                    return false;
                  }
                }
              }
            }
          }
          return true;
        } else {
          if (!(network instanceof Server)) {
            System.out.println("Client: Not doing a possible move! Move: counter=" + moveCounter + " Start: [" + start[0] + "|" + start[1] + "] Stop: [" + stop[0] + "|" + stop[1] + "]");
          }
          return false;
        }
      } else {
        if (!(network instanceof Server)) {
          System.out.println("Client: Not doing a move. Start = Stop: [" + start[0] + "|" + start[1] + "] [" + stop[0] + "|" + stop[1] + "] moveCounter: " + moveCounter + " start-stop: " + (stop[0] - start[0]) + "|" + (stop[1] - start[1]));
        }
        return false;
      }
    } else {
      if (!(network instanceof Server)) {
        System.out.println("Client: Field start: [" + start[0] + "|" + start[1] + "] or field: [" + stop[0] + "|" + stop[1] + "] does not exist!");
      }
      return false;
    }
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
      System.out.println("Performing punishment: " + punishment.getName() + " for the player: " + player.getName() + " and the reason: " + reason.getName());
    }
    MoleGames.getMoleGames().getServer().sendToAllGameClients(player.getGame(), MoleGames.getMoleGames().getServer().getPacketHandler().movePenaltyNotification(player, player.getGame().getDeductedPoints(), punishment, reason.getName()));
    if (punishment == Punishments.POINTS) {
      player.getGame().getScore().getPoints().put(player.getServerClient().getThreadID(), player.getGame().getScore().getPoints().get(player.getServerClient().getThreadID()) - player.getGame().getDeductedPoints());
    } else if (punishment == Punishments.KICK) {
      MoleGames.getMoleGames().getServer().getPacketHandler().playerKickedPacket(player, player.getGame());
      MoleGames.getMoleGames().getServer().getPacketHandler().removeFromGames((ServerThread) player.getServerClient());
    }
  }

  /**
   * @param player
   * @return if it could be moved or not
   * @author Carina
   * @use moves a mole depending on in a hole or on a field
   */
  public synchronized boolean isPlayerMovePossible(@NotNull final Player player) {
    for (var mole : player.getMoles()) {
      if (mole == null) {
        continue;
      }
      if (isMoleMoveable(player, mole)) {
        return true;
      }
    }
    System.out.println("Server: No move possible for this Player!");
    return false;
  }

  /**
   * @param cardValue
   * @param mole
   * @param direction
   * @return the end field of the move
   * @author Carina
   * @see Player
   */
  private synchronized int[] moleEndField(final int cardValue, @NotNull final Mole mole, @NotNull final Directions direction) {
    var x = 0;
    var y = 0;
    if (direction == Directions.UP) {
      y = mole.getPosition().getY() + cardValue;
      x = mole.getPosition().getX();
    } else if (direction == Directions.DOWN) {
      y = mole.getPosition().getY() - cardValue;
      x = mole.getPosition().getX();
    } else if (direction == Directions.LEFT) {
      y = mole.getPosition().getY();
      x = mole.getPosition().getX() - cardValue;
    } else if (direction == Directions.RIGHT) {
      y = mole.getPosition().getY();
      x = mole.getPosition().getX() + cardValue;
    } else if (direction == Directions.UP_LEFT) {
      y = mole.getPosition().getY() - cardValue;
      x = mole.getPosition().getX() - cardValue;
    } else if (direction == Directions.DOWN_RIGHT) {
      y = mole.getPosition().getY() + cardValue;
      x = mole.getPosition().getX() + cardValue;
    }
    return new int[]{x, y};
  }

  /**
   * @param player
   * @param mole
   * @return says if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  private synchronized boolean isMoleMoveable(@NotNull final Player player, @NotNull final Mole mole) throws NullPointerException {
    for (var direction : Directions.values()) {
      if (!player.getGame().getSettings().isPullDiscsOrdered()) {
        for (var cardValue : player.getCards()) {
          if (directionFinder(player, mole, direction, cardValue)) {
            return true;
          }
        }
      } else {
        if (directionFinder(player, mole, direction, player.getCards().get(0))) {
          return true;
        }
      }
    }
    return false;
  }

  private synchronized boolean directionFinder(@NotNull final Player player, @NotNull final Mole mole, @NotNull final Directions direction, final int cardValue) {
    var endField = moleEndField(cardValue, mole, direction);
    if (!player.getGame().getMap().getFieldMap().containsKey(List.of(endField[0], endField[1]))) {
      return false;
    }
    if (direction == Directions.DOWN) {
      try {
        if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(), new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, cardValue, player.getGame().getMap())) {
          return true;
        }
      } catch (@NotNull final Exception ignored) {
      }
    } else if (direction == Directions.UP) {
      try {
        if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(), new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, cardValue, player.getGame().getMap())) {
          return true;
        }
      } catch (@NotNull final Exception ignored) {
      }
    } else if (direction == Directions.LEFT) {
      try {
        if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(), new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, cardValue, player.getGame().getMap())) {
          return true;
        }
      } catch (@NotNull final Exception ignored) {
      }
    } else if (direction == RIGHT) {
      try {
        if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(), new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, cardValue, player.getGame().getMap())) {
          return true;
        }
      } catch (@NotNull final Exception ignored) {
      }
    } else if (direction == Directions.DOWN_RIGHT) {
      try {
        if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(), new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, cardValue, player.getGame().getMap())) {
          return true;
        }
      } catch (@NotNull final Exception ignored) {
      }
    } else if (direction == Directions.UP_LEFT) {
      try {
        if (GameLogic.wasLegalMove(MoleGames.getMoleGames().getServer(), new int[]{mole.getPosition().getX(), mole.getPosition().getY()}, endField, cardValue, player.getGame().getMap())) {
          return true;
        }
      } catch (@NotNull final Exception ignored) {
      }
    }
    return false;
  }
}
