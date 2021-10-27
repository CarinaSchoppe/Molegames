package network.util;

import java.net.Socket;

public abstract class Network {
  protected int port;
  protected Socket socket;
  protected String ip;

  /**
   * Basic logic of creating a network instance with default values for
   * @see Network port and ip with defaults 291220 and "localhost"
   */
  public Network() {
    port = 291220;
    ip = "localhost";
  }

  /**
   * @author Carina
   * @param port for a network instance
   * @param ip for a network instance
   */
  public Network(int port, String ip) {
    this.port = port;
    this.ip = ip;
  }

  protected int getPort() {
    return port;
  }

  protected String getIp() {
    return ip;
  }
}
