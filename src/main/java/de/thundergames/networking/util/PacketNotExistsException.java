package de.thundergames.networking.util;

import org.jetbrains.annotations.NotNull;

public class PacketNotExistsException extends Exception {

  private static final long serialVersionUID = 1L;

  public PacketNotExistsException(@NotNull final String message) {
    super(message);
  }
}
