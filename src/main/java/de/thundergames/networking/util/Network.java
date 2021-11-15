/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.networking.util;

import java.net.Socket;
import org.jetbrains.annotations.NotNull;

public abstract class Network {
  protected final int port;
  protected final String ip;
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
   * @param ip for a de.thundergames.network instance
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
   * @see de.thundergames.networking.client.Client
   */
  protected abstract void create();

  protected int getPort() {
    return port;
  }
}
