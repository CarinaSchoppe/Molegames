package de.thundergames.networking.util.interfaceItems;

public class NetworkField extends InterfaceObject{

  private final int x;
  private final int y;

  public NetworkField(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
