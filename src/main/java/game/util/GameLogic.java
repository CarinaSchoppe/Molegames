package game.util;

import game.map.Field;

import java.util.ArrayList;

public class GameLogic {

  /**
   * @param start       the startpoint in form of x and y
   * @param stop        the endpoint where the player wants to go in form of x and y
   * @param moveCounter the amounts of fields the player can move
   * @param fields      the fields of the playingfield to check if a field was accupied by a player to the given player cant move like that
   * @return if the move is valid
   */
  private synchronized boolean wasLegalMove(int[] start, int[] stop, int moveCounter, ArrayList<Field> occupiedFields) {
    //check if player moved to much
    if (stop[0] - start[0] == 0 && Math.abs(stop[1] - start[1]) == moveCounter || start[1] - stop[1] == 0 && Math.abs(stop[0] - start[0]) == moveCounter || Math.abs(stop[0] - start[0]) == Math.abs(stop[1] - start[1]) && Math.abs(start[1] - stop[1]) == moveCounter) {
      for (Field field : occupiedFields) {
        if (stop[0] - start[0] == 0) {
          for (int i = 1; i < moveCounter; i++) {
            if (stop[1] - start[1] > 0) {
              if (field.getY() == start[1] + i) {
                return false;
              }
            } else {
              if (field.getY() == start[1] - i) {
                return false;
              }
            }
          }
        } else if (stop[1] - start[1] == 0) {
          for (int i = 1; i < moveCounter; i++) {
            if (start[0] - start[0] > 0) {
              if (field.getX() == start[0] + i) {
                return false;
              }
            } else {
              if (field.getX() == start[0] - i) {
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
              if (field.getX() == start[0] + i && field.getY() == start[1] - i) {
                return false;
              }
            } else if (stop[0] - start[0] < 0 && stop[1] - start[1] < 0) {
              if (field.getX() == start[0] - i && field.getY() == start[1] + i) {
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
    return false;
  }
}
