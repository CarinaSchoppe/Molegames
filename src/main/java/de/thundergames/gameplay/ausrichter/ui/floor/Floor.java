/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 16:39 by Carina Latest changes made by Carina on 21.12.21, 16:37 All contents of "Floor" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui.floor;

import de.thundergames.gameplay.ausrichter.ui.CreateGame;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Floor {

  private final ArrayList<Hole> holes = new ArrayList<>();
  private final ArrayList<DrawAgain> drawAgain = new ArrayList<>();
  private final int points;

  public String getPointsString() {
    return Integer.toString(points);
  }

  public String holeAmountString() {
    return Integer.toString(holes.size());
  }

  public String drawAgainAmountString() {
    return Integer.toString(drawAgain.size());
  }

  public String floorNumberString() {
    return Integer.toString(CreateGame.getFloors().indexOf(this));
  }
}
