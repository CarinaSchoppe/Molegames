/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 16:05 by Carina Latest changes made by Carina on 09.01.22, 16:05 All contents of "Hole" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui.floor;

import com.google.gson.annotations.SerializedName;
import de.thundergames.gameplay.ausrichter.ui.CreateGame;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hole {
  private final transient Floor floor;

  @SerializedName(value = "x")
  private int xPosition;

  @SerializedName(value = "y")
  private int yPosition;

  public String getXPositionString() {
    return Integer.toString(xPosition);
  }

  public String getYPositionString() {
    return Integer.toString(yPosition);
  }

  public String getHoleValueString() {
    for (var floor : CreateGame.getFloors()) {
      if (floor.getHoles().contains(this)) {
        return Integer.toString(floor.getHoles().indexOf(this));
      }
    }
    return "";
  }
}
