/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:53
 * All contents of "AIUtil" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai;

import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.map.Map;
import de.thundergames.playmechanics.util.Mole;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AIUtil {


  /**
   * @param ai
   * @return
   * @author Carina
   * @use returns the map object created on the base of the jsonString
   */
  public Map createMapFromJson(@NotNull final AI ai) {
    var map = new Map(ai.getGameState());
    map.changeFieldParams(ai.getGameState());
    return map;
  }


  /**
   * @param ai
   * @param field that will be checked with the mole
   * @param mole  that will be checked with the field
   * @return the distance between the mole and the field in form of X and Y
   * @author Carina
   * @use input the parameter and than returns the distance between the mole and the field
   */
  public List<Integer> getDistance(@NotNull final AI ai, @NotNull final Field field, final Mole mole) {
    var x = mole.getNetworkField().getX();
    var y = mole.getNetworkField().getY();
    var distanceX = Math.abs(field.getX() - x);
    var distanceY = Math.abs(field.getY() - y);
    return List.of(distanceX, distanceY);
  }

}
