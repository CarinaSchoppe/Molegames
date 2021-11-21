/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "NetworkFloor" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

import java.util.ArrayList;

public class NetworkFloor extends InterfaceObject {

  private  ArrayList<NetworkField> holes  = new ArrayList<>();
  private  ArrayList<NetworkField> drawAgainFields  = new ArrayList<>();
  private int points;


  public int getPoints() {
    return points;
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

  public void setPoints(int points) {
    this.points = points;
  }
}
