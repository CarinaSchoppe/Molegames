package network.util;

public enum Packets {

  CREATEGAME(0, "CREATE-GAME"),
  JOINGAME(1, "JOIN-GAME"),
  GIVEID(2, "ID"),
  JOINEDGAME(3, "JOINED-GAME"),
  MESSAGE(4, "MESSAGE"),
  DISCONNECT(5, "DISCONNECT");

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
