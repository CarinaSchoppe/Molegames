/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 21:41 by Carina latest changes made by Carina on 22.11.21, 19:45 All contents of "NetworkMole" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

public class NetworkMole {

  private final NetworkPlayer player;
  private NetworkField field;

  public NetworkMole(NetworkPlayer player, NetworkField field) {
    this.player = player;
    this.field = field;
  }

  public NetworkPlayer getPlayer() {
    return player;
  }

  public NetworkField getNetworkField() {
    return field;
  }

  public void setNetworkField(NetworkField field) {
    this.field = field;
  }


}
