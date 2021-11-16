/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 15:51 by Carina
 * Latest changes made by Carina on 15.11.21, 15:10
 * All contents of "Field" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

import de.thundergames.playmechanics.util.Mole;
import java.util.List;

import de.thundergames.playmechanics.util.Player;
import org.jetbrains.annotations.NotNull;

public class Field {
  private final List<Integer> id;
  private final boolean doubleMove = false;
  private boolean occupied;
  private boolean hole;
  private Mole mole;

  public Field(@NotNull final List<Integer> id) {
    this.id = id;
  }

  public List<Integer> getId() {
    return id;
  }

  @Override
  public String toString() {
    if (isOccupied())
      return "field x: "
          + id.get(0)
          + " y: "
          + id.get(1)
          + isOccupied()
          + " occupied by "
          + mole.getPlayer().getServerClient().getClientName()
          + " hole"
          + isHole()
          + " doubleMove"
          + isDoubleMove();
    else
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

  public int getY() {
    return id.get(1);
  }

  public int getX() {
    return id.get(0);
  }

  /**
   * @param occupied if a field is occupied by a mole from a player
   * @param mole the mole that occupies the field
   * @author Carina
   * @see Mole
   * @see Player
   */
  public void setOccupied(final boolean occupied, Mole mole) {
    if (occupied) this.mole = mole;
    else this.mole = null;
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

  public Mole getMole() {
    return mole;
  }

  public boolean isDoubleMove() {
    return doubleMove;
  }
}
