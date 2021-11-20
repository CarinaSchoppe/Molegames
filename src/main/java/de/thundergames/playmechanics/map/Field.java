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
import de.thundergames.playmechanics.util.interfaceItems.NetworkField;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Field extends NetworkField {

  private final List<Integer> field;
  private boolean drawAgainField = false;
  private boolean occupied;
  private boolean hole;
  private int moleID;
  private Map map;


  public Field(@NotNull final List<Integer> field) {
    super(field.get(0), field.get(1));
    this.field = field;
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
          + field.get(0)
          + " y: "
          + field.get(1)
          + isOccupied()
          + " occupied by "
          + map.getGame().getMoleIDMap().get(moleID).getPlayer().getServerClient().getClientName()
          + " hole"
          + isHole()
          + " drawAgainField"
          + isDrawAgainField();
    } else {
      return "field x: "
          + field.get(0)
          + " y: "
          + field.get(1)
          + isOccupied()
          + " hole"
          + isHole()
          + " drawAgainField"
          + isDrawAgainField();
    }
  }


  public void setOccupied(final boolean occupied, int moleID) {
    this.moleID = moleID;
    this.occupied = occupied;
  }



  public int getY() {
    return field.get(1);
  }

  public int getX() {
    return field.get(0);
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

  public int getMoleID() {
    return moleID;
  }

  public List<Integer> getField() {
    return field;
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
