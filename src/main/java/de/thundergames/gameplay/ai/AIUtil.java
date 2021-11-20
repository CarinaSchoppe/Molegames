/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 09:41
 * All contents of "AIUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AIUtil {


  /**
   * @param ai
   * @return
   * @author Carina
   * @use returns the map object created on the base of the jsonString
   */
  public Map createMapFromJson(@NotNull final AI ai) {
//TODO hier
    return null;
  }


  /**
   * @param ai
   * @author Carina
   * @use changes the the lists for moles in the hole and the moles on the field
   */
  public void changeMoleFieldTypes(@NotNull final AI ai) {
    for (var field : ai.getMap().getFields()) {
      if (field.isOccupied()) {
        if (ai.getPlayerMolesOnField().contains(field.getMoleID())) {
          if (field.isHole()) {
            ai.getPlayerMolesOnField().remove(field.getMoleID());
            ai.getPlayerMolesInHoles().add(field.getMoleID());
          } else {
            ai.getPlayerMolesInHoles().remove(field.getMoleID());
            ai.getPlayerMolesOnField().add(field.getMoleID());
          }
        }
      }
    }
  }

  /**
   * @param map the floor of the map with all that fields
   * @param field the field itself
   * @author Carina
   * @use sets the items on the field if its occupied and when by what moleID, a hole, a draw again fields etc.
   */
  private void setFieldItems(@NotNull final AI ai, @NotNull final Map map, @NotNull final Field field) {

//TODO: hier
  }

  /**
   * @param ai
   * @param field  that will be checked with the mole
   * @param moleID that will be checked with the field
   * @return the distance between the mole and the field in form of X and Y
   * @author Carina
   * @use input the parameter and than returns the distance between the mole and the field
   */
  public List<Integer> getDistance(@NotNull final AI ai, @NotNull final Field field, final int moleID) {
    var mole = ai.getMap().getFieldMap().get(ai.getMolePositions().get(moleID));
    var x = mole.getField().get(0);
    var y = mole.getField().get(1);
    var distanceX = Math.abs(field.getField().get(0) - x);
    var distanceY = Math.abs(field.getField().get(1) - y);
    return List.of(distanceX, distanceY);
  }

}
