/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 14.12.21, 15:41 by Carina Latest changes made by Carina on 14.12.21, 15:41 All contents of "NetworkFloor" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceitems;

import java.util.HashSet;

public class NetworkFloor {

  private HashSet<NetworkField> holes;
  private HashSet<NetworkField> drawAgainFields;
  private int points = 1;

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public HashSet<NetworkField> getHoles() {
    return holes;
  }

  public void setHoles(HashSet<NetworkField> holes) {
    this.holes = holes;
  }

  public HashSet<NetworkField> getDrawAgainFields() {
    return drawAgainFields;
  }

  public void setDrawAgainFields(HashSet<NetworkField> drawAgainFields) {
    this.drawAgainFields = drawAgainFields;
  }
}
