/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "Mole" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.util;

import de.thundergames.playmechanics.map.Field;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Mole {

  private final int MoleID;
  private final Player player;
  private final boolean inHole = false;
  private Field field;

  public Mole(final int moleID, @NotNull final Player player) {
    this.player = player;
    MoleID = moleID;
  }

  public int getMoleID() {
    return MoleID;
  }

  /**
   * @return sais if a mole can be moved (important for AI)
   * @author Carina
   * @see Player
   * @see de.thundergames.gameplay.ai.AI
   */
  public boolean isMoveable(int cardValue) {
    for (var direction : Directions.values()) {
      if (direction == Directions.DOWN) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX(), field.getY() - cardValue))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX(), field.getY() - cardValue)).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.UP) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX(), field.getY() + cardValue))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX(), field.getY() + cardValue)).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.LEFT) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX() - cardValue, field.getY()))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX() - cardValue, field.getY())).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.RIGHT) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX() + cardValue, field.getY()))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX() + cardValue, field.getY())).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.DOWN_LEFT) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX() - cardValue, field.getY() - cardValue))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX() - cardValue, field.getY() - cardValue)).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.DOWN_RIGHT) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX() + cardValue, field.getY() - cardValue))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX() + cardValue, field.getY() - cardValue)).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.UP_LEFT) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX() - cardValue, field.getY() + cardValue))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX() - cardValue, field.getY() + cardValue)).isOccupied();
        } else {
          return false;
        }
      } else if (direction == Directions.UP_RIGHT) {
        if (field.getFloor().getFieldMap().containsKey(List.of(field.getX() + cardValue, field.getY() + cardValue))) {
          return field.getFloor().getFieldMap().get(List.of(field.getX() + cardValue, field.getY() + cardValue)).isOccupied();
        } else {
          return false;
        }
      }
    }
    return false;
  }

  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Player getPlayer() {
    return player;
  }
}
