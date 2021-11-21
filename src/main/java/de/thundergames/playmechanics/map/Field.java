/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "Field" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.playmechanics.map;
import de.thundergames.networking.util.interfaceItems.NetworkField;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Field extends NetworkField {

  private boolean drawAgainField = false;
  private boolean occupied;
  private boolean hole;
  private Map map;


  public Field(@NotNull final List<Integer> field) {
    super(field.get(0), field.get(1));
  }


  public void setOccupied(final boolean occupied) {
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


}
