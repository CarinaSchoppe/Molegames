package de.thundergames.network.util;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public abstract class Network {
  protected final int port;
  protected Socket socket;
  protected final String ip;

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
   * @see de.thundergames.network.server.Server
   * @see de.thundergames.network.client.Client
   */
  protected abstract void create();

  protected int getPort() {
    return port;
  }
}
