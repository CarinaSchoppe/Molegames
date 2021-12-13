/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 03.12.21, 15:04 by Carina latest changes made by Carina on 03.12.21, 15:04
 * All contents of "Network" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

import de.thundergames.gameplay.player.Client;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public abstract class Network {

  protected final int port;
  protected final String ip;
  private final boolean debug = true;
  protected Socket socket;

  /**
   * Basic logic of creating a de.thundergames.network instance with default values for
   *
   * @see Network port and ip with defaults 291220 and "localhost"
   */
  public Network() {
    port = 5000;
    ip = "127.0.0.1";
  }

  /**
   * @param port for a de.thundergames.network instance
   * @param ip   for a de.thundergames.network instance
   * @author Carina
   */
  public Network(final int port, @NotNull final String ip) {
    this.port = port;
    this.ip = ip;
  }

  /**
   * @author Carina
   * @use the abstract logic of creating the sockets and threads for the server communicationlogic
   * @see de.thundergames.networking.server.Server
   * @see Client
   */
  protected abstract void create();

  public boolean isDebug() {
    return debug;
  }

  protected int getPort() {
    return port;
  }
}
