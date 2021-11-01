package network.util;

public enum Packets {

  CREATEGAME(0, "MANAGEMENT", "CREATE-GAME"),
  JOINGAME(1, "MANAGEMENT", "JOIN-GAME"),
  GIVEID(2, "MANAGEMENT", "ID"),
  JOINEDGAME(3, "MESSAGE", "JOINED-GAME");

  private final int id;
  private final String name;
  private final String content;

  Packets(int id, String name, String content) {
    this.id = id;
    this.name = name;
    this.content = content;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }
}
