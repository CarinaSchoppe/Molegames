/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 15:53 by Carina latest changes made by Carina on 02.12.21, 15:49
 * All contents of "NetworkMessage" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

import java.util.HashMap;

public class NetworkMessage {

  private final String type;
  private final HashMap<String, Object> value = new HashMap<>();

  public NetworkMessage(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public HashMap<String, Object> getValue() {
    return value;
  }
}
