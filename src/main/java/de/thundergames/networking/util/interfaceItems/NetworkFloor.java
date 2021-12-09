/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:49
 * All contents of "NetworkFloor" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

import java.util.ArrayList;

public class NetworkFloor {

  private ArrayList<NetworkField> holes;
  private ArrayList<NetworkField> drawAgainFields;
  private int points = 1;

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public ArrayList<NetworkField> getHoles() {
    return holes;
  }

  public void setHoles(ArrayList<NetworkField> holes) {
    this.holes = holes;
  }

  public ArrayList<NetworkField> getDrawAgainFields() {
    return drawAgainFields;
  }

  public void setDrawAgainFields(ArrayList<NetworkField> drawAgainFields) {
    this.drawAgainFields = drawAgainFields;
  }
}
