package de.thundergames.gameplay.ai;

import static de.thundergames.playmechanics.util.Directions.DOWN_LEFT;
import static de.thundergames.playmechanics.util.Directions.RIGHT;

import de.thundergames.playmechanics.util.Directions;
import de.thundergames.playmechanics.util.Player;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AILogic {


  /**
   * @return sais if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public Directions isMoveable(@NotNull final AI ai, final int cardValue, @NotNull final List<Integer> field) throws NullPointerException {
    for (var direction : Directions.values()) {
      if (direction == Directions.DOWN) {
        try {
          if (!ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0), field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN;
          }
        } catch (Exception e) {

        }
      } else if (direction == Directions.UP) {
        try {
          if (!ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0), field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP;
          }
        } catch (Exception e) {
        }
      } else if (direction == Directions.LEFT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1))).isOccupied()) {
            return Directions.LEFT;
          }
        } catch (Exception e) {

        }
      } else if (direction == RIGHT) {

        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1))).isOccupied()) {
            return Directions.RIGHT;
          }
        } catch (Exception e) {

        }
      } else if (direction == DOWN_LEFT) {

        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN_LEFT;
          }
        } catch (Exception e) {

        }
      } else if (direction == Directions.DOWN_RIGHT) {

        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) - cardValue)).isOccupied()) {
            return Directions.DOWN_RIGHT;
          }
        } catch (Exception e) {

        }
      } else if (direction == Directions.UP_LEFT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) - cardValue, field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP_LEFT;
          }
        } catch (Exception e) {

        }
      } else if (direction == Directions.UP_RIGHT) {
        try {
          if (ai.getMap().getFloor().getFieldMap().get(List.of(field.get(0) + cardValue, field.get(1) + cardValue)).isOccupied()) {
            return Directions.UP_RIGHT;
          }
        } catch (Exception e) {

        }
      }
    }
    return null;

  }
}

