package de.thundergames.network.util;

/**
 * @author Carina
 * @see Packets the packet element that can be send
 */
public enum Packets {

  CREATEGAME(0, "CREATE-GAME"),
  JOINGAME(1, "JOIN-GAME"),
  GIVEID(2, "ID"),
  JOINEDGAME(3, "JOINED-GAME"),
  MESSAGE(4, "MESSAGE"),
  DISCONNECT(5, "DISCONNECT"),
  ERROR(6, "ERROR"),
  FULL(7, "FULL"),
  INGAME(8, "INGAME"),
  NOTEXISTS(9, "NOT-EXISTS");

  private final int id;
  private final String packetType;

  Packets(int id, String packetType) {
    this.id = id;
    this.packetType = packetType;
  }

  public int getId() {
    return id;
  }

  public String getPacketType() {
    return packetType;
  }
}
