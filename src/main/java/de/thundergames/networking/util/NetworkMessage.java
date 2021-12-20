/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 20.12.21, 16:43 by Carina Latest changes made by Carina on 20.12.21, 16:18 All contents of "NetworkMessage" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util;

import lombok.Data;

import java.util.HashMap;

@Data
public class NetworkMessage {

  private final String type;
  private final HashMap<String, Object> value = new HashMap<>();
}
