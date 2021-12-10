/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:53
 * All contents of "Field" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;

import de.thundergames.networking.util.interfaceItems.NetworkField;
import de.thundergames.networking.util.interfaceItems.NetworkMole;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Field extends NetworkField {

  private transient boolean drawAgainField = false;
  private transient boolean occupied;
  private transient boolean hole;
  private transient Map map;
  private NetworkMole mole;

  public Field(@NotNull final List<Integer> field) {
    super(field.get(0), field.get(1));
  }

  public boolean isOccupied() {
    return occupied;
  }

  public void setOccupied(final boolean occupied) {
    this.occupied = occupied;
  }

  public boolean isHole() {
    return hole;
  }

  public void setHole(boolean hole) {
    this.hole = hole;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map floor) {
    this.map = floor;
  }

  public boolean isDrawAgainField() {
    return drawAgainField;
  }

  public void setDrawAgainField(boolean drawAgainField) {
    this.drawAgainField = drawAgainField;
  }

  public NetworkMole getMole() {
    return mole;
  }

  public void setMole(NetworkMole mole) {
    this.mole = mole;
  }
}
