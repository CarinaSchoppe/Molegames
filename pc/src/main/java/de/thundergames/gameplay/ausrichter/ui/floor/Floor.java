/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 17:11 by Carina Latest changes made by Carina on 20.01.22, 17:11 All contents of "Floor" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui.floor;

import de.thundergames.gameplay.ausrichter.ui.CreateGame;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author Carina
 * @use the object behind a Floor for @see CreateGame
 * @see CreateGame
 * @see DrawAgain
 * @see Hole
 */
@Data
public class Floor {

  private final ArrayList<Hole> holes = new ArrayList<>();
  private final ArrayList<DrawAgain> drawAgainFields = new ArrayList<>();
  private final int points;

  public String getPointsString() {
    return Integer.toString(points);
  }

  public String holeAmountString() {
    return Integer.toString(holes.size());
  }

  public String drawAgainFieldsAmountString() {
    return Integer.toString(drawAgainFields.size());
  }

  public String floorNumberString() {
    return Integer.toString(CreateGame.getFloors().indexOf(this));
  }
}
