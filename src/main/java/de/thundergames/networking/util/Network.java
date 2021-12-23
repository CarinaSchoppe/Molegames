/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 16:39 by Carina Latest changes made by Carina on 21.12.21, 16:37 All contents of "Network" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

import de.thundergames.gameplay.player.Client;
import lombok.Data;

import java.net.Socket;

@Data
public abstract class Network {

  protected final int port;
  protected final String ip;
  protected Socket socket;
  private boolean debug = true;

  /**
   * @author Carina
   * @use the abstract logic of creating the sockets and threads for the server communicationlogic
   * @see de.thundergames.networking.server.Server
   * @see Client
   */
  protected abstract void create();
}
