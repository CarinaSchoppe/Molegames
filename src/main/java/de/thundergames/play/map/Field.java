/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.play.map;

import de.thundergames.play.util.Mole;
import java.util.List;
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
    return "field x: " + id.get(0) + " y: " + id.get(1);
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
   * @see de.thundergames.gameplay.player.Player
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

  public Mole getMole() {
    return mole;
  }

  public boolean isDoubleMove() {
    return doubleMove;
  }
}
