package de.thundergames.network.util;

public class PacketNotExistsException extends Exception {

  private static final long serialVersionUID = 1L;

  public PacketNotExistsException(String message) {
    super(message);
  }
  public PacketNotExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
