package de.thundergames.playmechanics.util.interfaceItems;

public class NetworkMole extends InterfaceObject {

  private final NetworkPlayer player;
  private NetworkField field;

  public NetworkMole(NetworkPlayer player, NetworkField field) {
    this.player = player;
    this.field = field;
  }

  public NetworkPlayer getPlayer() {
    return player;
  }

  public NetworkField getField() {
    return field;
  }


  public void setNetworkField(NetworkField field) {
    this.field = field;
  }


}
