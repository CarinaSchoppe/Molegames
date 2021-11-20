package de.thundergames.networking.util.interfaceItems;

public class NetworkPlayer extends InterfaceObject {

  private final String name;
  private final int clientID;

  public NetworkPlayer(String name, int clientID) {
    this.name = name;
    this.clientID = clientID;
  }

  public String getName() {
    return name;
  }

  public int getClientID() {
    return clientID;
  }


}
