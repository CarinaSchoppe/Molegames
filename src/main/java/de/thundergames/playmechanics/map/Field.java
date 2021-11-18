/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 10:32
 * All contents of "Field" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
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


  /**
   * @return the field in a string format
   * @author Carina
   * @use call to turn a field into a string
   */
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


  public int getY() {
    return id.get(1);
  }

  public int getX() {
    return id.get(0);
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
