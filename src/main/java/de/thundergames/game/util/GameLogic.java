package de.thundergames.game.util;

import de.thundergames.game.map.Map;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {

  /**
   * @param start       the startpoint in form of x and y
   * @param stop        the endpoint where the player wants to go in form of x and y
   * @param moveCounter the amounts of fields the player can move
   * @param map         of the de.thundergames.game and of the player
   * @return if the move is valid it will return true
   * @use add the parameters and it will return if the move was valid with true or invalid with false
   * @premisse the startpoint and endpoint must be in the playingfield and the player was allowed to move.
   */
  public synchronized boolean wasLegalMove(final List<Integer> start, final List<Integer> stop, final int moveCounter, @NotNull final Map map) {
    //check if player moved to much
    if (map.getFloor().getFieldMap().containsKey(start) && map.getFloor().getFieldMap().containsKey(stop) && start != stop) {
      if (stop.get(0) - start.get(0) == 0 && Math.abs(stop.get(1) - start.get(1)) == moveCounter || start.get(1) - stop.get(1) == 0 && Math.abs(stop.get(0) - start.get(0)) == moveCounter || Math.abs(stop.get(0) - start.get(0)) == Math.abs(stop.get(1) - start.get(1)) && Math.abs(start.get(1) - stop.get(1)) == moveCounter) {
        for (var field : map.getFloor().getOccupied()) {
          if (stop.get(0) - start.get(0) == 0) {
            for (var i = 1; i < moveCounter; i++) {
              if (stop.get(1) - start.get(1) > 0) {
                if (field.getY() == start.get(1) + i && field.getX() == start.get(0)) {
                  return false;
                }
              } else if (stop.get(1) - start.get(1) < 0) {
                if (field.getY() ==start.get(1) - i && field.getX() == start.get(0)) {
                  return false;
                }
              }
            }
          } else if (stop.get(1)- start.get(1) == 0) {
            for (var i = 1; i < moveCounter; i++) {
              if (stop.get(0) - start.get(0) > 0) {
                if (field.getX() == start.get(0) + i && field.getY() == start.get(1)) {
                  return false;
                }
              } else if (stop.get(0) - start.get(0) < 0) {
                if (field.getX() == start.get(0) - i && field.getY() == start.get(1)) {
                  return false;
                }
              }
            }
          } else if (Math.abs(stop.get(0) - start.get(0)) == Math.abs(stop.get(1) - start.get(1))) {
            for (var i = 1; i < moveCounter; i++) {
              if (stop.get(1) - start.get(1) > 0 && stop.get(0) - start.get(0) > 0) {
                if (field.getX() == start.get(0) + i && field.getY() ==start.get(1) + i) {
                  return false;
                }
              } else if (stop.get(0) - start.get(0) < 0 && stop.get(1) - start.get(1) > 0) {
                if (field.getX() == start.get(0) - i && field.getY() == start.get(1) + i) {
                  return false;
                }
              } else if (stop.get(0) - start.get(0) > 0 && stop.get(1) - start.get(1) < 0) {
                if (field.getX() == start.get(0) + i && field.getY() == start.get(1) - i) {
                  return false;
                }
              } else if (stop.get(0) - start.get(0) < 0 && stop.get(1) - start.get(1) < 0) {
                if (field.getX() == start.get(0) - i && field.getY() == start.get(1) - i) {
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
}
