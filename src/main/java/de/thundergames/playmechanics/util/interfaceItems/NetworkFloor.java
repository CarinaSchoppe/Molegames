package de.thundergames.playmechanics.util.interfaceItems;

public class NetworkFloor extends InterfaceObject{

  private NetworkField[] holes;
  private NetworkField[] drawAgainFields;
  private int points;

  public NetworkField[] getHoles() {
    return holes;
  }

  public NetworkField[] getDrawAgainFields() {
    return drawAgainFields;
  }

  public int getPoints() {
    return points;
  }

  public void setHoles(NetworkField[] holes) {
    this.holes = holes;
  }

  public void setDrawAgainFields(NetworkField[] drawAgainFields) {
    this.drawAgainFields = drawAgainFields;
  }

  public void setPoints(int points) {
    this.points = points;
  }
}
