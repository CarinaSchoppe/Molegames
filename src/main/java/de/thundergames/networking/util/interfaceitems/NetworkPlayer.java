/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 14.12.21, 15:41 by Carina Latest changes made by Carina on 14.12.21, 15:41 All contents of "NetworkPlayer" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceitems;

import org.jetbrains.annotations.NotNull;

public class NetworkPlayer {

  private final int clientID;
  private final String name;

  public NetworkPlayer(@NotNull final String name, final int clientID) {
    this.name = name;
    this.clientID = clientID;
  }

  public String getName() {
    return name;
  }

  public int getClientID() {
    return clientID;
  }

  @Override
  public String toString() {
    return "Playermodel with the name: " + name + " and clientID: " + clientID + "";
  }
}
