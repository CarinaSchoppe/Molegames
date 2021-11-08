package game.util;

import game.map.Field;
import game.map.Map;
import org.jetbrains.annotations.NotNull;

public class GameLogic {

  /**
   * @param start       the startpoint in form of x and y
   * @param stop        the endpoint where the player wants to go in form of x and y
   * @param moveCounter the amounts of fields the player can move
   * @param map         of the game and of the player
   * @return if the move is valid it will return true
   * @use add the parameters and it will return if the move was valid with true or invalid with false
   * @premisse the startpoint and endpoint must be in the playingfield and the player was allowed to move.
   */
  private synchronized boolean wasLegalMove(final int[] start, final int[] stop, int moveCounter, @NotNull final Map map) {
    //check if player moved to much
    if (map.getFieldCounter().containsKey(start) && map.getFieldCounter().containsKey(stop) && start != stop) {
      if (stop[0] - start[0] == 0 && Math.abs(stop[1] - start[1]) == moveCounter || start[1] - stop[1] == 0 && Math.abs(stop[0] - start[0]) == moveCounter || Math.abs(stop[0] - start[0]) == Math.abs(stop[1] - start[1]) && Math.abs(start[1] - stop[1]) == moveCounter) {
        for (Field field : map.getOccupied()) {
          if (stop[0] - start[0] == 0) {
            for (int i = 1; i < moveCounter; i++) {
              if (stop[1] - start[1] > 0) {
                if (field.getY() == start[1] + i && field.getX() == start[0]) {
                  return false;
                }
              } else if (stop[1] - start[1] < 0) {
                if (field.getY() == start[1] - i && field.getX() == start[0]) {
                  return false;
                }
              }
            }
          } else if (stop[1] - start[1] == 0) {
            for (int i = 1; i < moveCounter; i++) {
              if (stop[0] - start[0] > 0) {
                if (field.getX() == start[0] + i && field.getY() == start[1]) {
                  return false;
                }
              } else if (stop[0] - start[0] < 0) {
                if (field.getX() == start[0] - i && field.getY() == start[1]) {
                  return false;
                }
              }
            }
          } else if (Math.abs(stop[0] - start[0]) == Math.abs(stop[1] - start[1])) {
            for (int i = 1; i < moveCounter; i++) {
              if (stop[1] - start[1] > 0 && stop[0] - start[0] > 0) {
                if (field.getX() == start[0] + i && field.getY() == start[1] + i) {
                  return false;
                }
              } else if (stop[0] - start[0] < 0 && stop[1] - start[1] > 0) {
                if (field.getX() == start[0] - i && field.getY() == start[1] + i) {
                  return false;
                }
              } else if (stop[0] - start[0] > 0 && stop[1] - start[1] < 0) {
                if (field.getX() == start[0] + i && field.getY() == start[1] - i) {
                  return false;
                }
              } else if (stop[0] - start[0] < 0 && stop[1] - start[1] < 0) {
                if (field.getX() == start[0] - i && field.getY() == start[1] - i) {
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
