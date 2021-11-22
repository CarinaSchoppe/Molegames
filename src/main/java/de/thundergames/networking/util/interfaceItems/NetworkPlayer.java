/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 21.11.21, 18:48 All contents of "NetworkPlayer" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

public class NetworkPlayer {

  private  String name;
  private final int clientID;

  public NetworkPlayer(String name, int clientID) {
    this.name = name;
    this.clientID = clientID;
  }

  public String getName() {
    return name;
  }

  public int getClientID() {
    return clientID;
  }

  public void setName(String name) {
    this.name = name;
  }
}
