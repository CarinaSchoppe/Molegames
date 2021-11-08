package network.util;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public abstract class Network {
  protected final int port;
  protected Socket socket;
  protected final String ip;

  /**
   * Basic logic of creating a network instance with default values for
   *
   * @see Network port and ip with defaults 291220 and "localhost"
   */
  public Network() {
    port = 5000;
    ip = "127.0.0.1";
  }

  /**
   * @param port for a network instance
   * @param ip   for a network instance
   * @author Carina
   */
  public Network(final int port, @NotNull final String ip) {
    this.port = port;
    this.ip = ip;
  }

  /**
   * @author Carina
   * @use the abstract logic of creating the sockets and threads for the server communicationlogic
   * @see network.server.Server
   * @see network.client.Client
   */
  protected abstract void create();

  protected int getPort() {
    return port;
  }

  protected String getIp() {
    return ip;
  }
}
