/*
 *
 *  *     / **
 *  *      *   Copyright Notice                                             *
 *  *      *   Copyright (c) SwtPra10 | ThunderGames 2021                         *
 *  *      *   Created: 05.05.2018 / 11:59                                  *
 *  *      *   All contents of this source text are protected by copyright. *
 *  *      *   The copyright law, unless expressly indicated otherwise, is  *
 *  *      *   at SwtPra10 | ThunderGames. All rights reserved                    *
 *  *      *   Any type of duplication, distribution, rental, sale, award,  *
 *  *      *   Public accessibility or other use                            *
 *  *      *   Requires the express written consent of SwtPra10 | ThunderGames.   *
 *  *      **
 *  *
 */
package de.thundergames.playmechanics.map;

import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Field {

  private final List<Integer> id;
  private boolean doubleMove = false;
  private boolean occupied;
  private boolean hole;
  private int mole;
  private Floors floor;


  public Field(@NotNull final List<Integer> id) {
    this.id = id;
  }


  @Override
  public String toString() {
    if (isOccupied()) {
      return "field x: "
          + id.get(0)
          + " y: "
          + id.get(1)
          + isOccupied()
          + " occupied by "
          + floor.getMap().getGame().getMoleIDMap().get(mole).getPlayer().getServerClient().getClientName()
          + " hole"
          + isHole()
          + " doubleMove"
          + isDoubleMove();
    } else {
      return "field x: "
          + id.get(0)
          + " y: "
          + id.get(1)
          + isOccupied()
          + " hole"
          + isHole()
          + " doubleMove"
          + isDoubleMove();
    }
  }

  public int getY() {
    return id.get(1);
  }

  public int getX() {
    return id.get(0);
  }

  /**
   * @param occupied if a field is occupied by a mole from a player
   * @param mole     the mole that occupies the field
   * @author Carina
   * @see Mole
   * @see Player
   */
  public void setOccupied(final boolean occupied, int mole) {
    if (occupied) {
      this.mole = mole;
    } else {
      this.mole = -1;
    }
    this.occupied = occupied;
  }


  public boolean isOccupied() {
    return occupied;
  }

  public boolean isHole() {
    return hole;
  }

  public void setHole(boolean hole) {
    this.hole = hole;
  }

  public int getMole() {
    return mole;
  }

  public List<Integer> getId() {
    return id;
  }

  public Floors getFloor() {
    return floor;
  }

  public void setFloor(Floors floor) {
    this.floor = floor;
  }

  public boolean isDoubleMove() {
    return doubleMove;
  }

  public void setDoubleMove(boolean doubleMove) {
    this.doubleMove = doubleMove;
  }
}
