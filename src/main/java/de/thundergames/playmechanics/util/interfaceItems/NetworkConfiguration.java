package de.thundergames.playmechanics.util.interfaceItems;

public class NetworkConfiguration extends InterfaceObject {

  private int playerCount = 0;
  private int radius = 5;
  private int numberOfMoles = 5;
  private NetworkFloor[] levels;
  private boolean pullDiscsOrdered = true;
  private int[] pullDiscs = new int[]{1, 3, 1, 2, 4, 3, 4, 3, 1, 2, 5, 3, 1, 3, 2};
  private int turnTime = 20;
  private int visualizationTime = 10;
  private String movePenalty = "NOTHING";



  public int getMaxPlayerCount() {
    return playerCount;
  }

  public void setMaxPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public int getNumberOfMoles() {
    return numberOfMoles;
  }

  public void setNumberOfMoles(int numberOfMoles) {
    this.numberOfMoles = numberOfMoles;
  }

  public NetworkFloor[] getLevels() {
    return levels;
  }

  public void setLevels(NetworkFloor[] levels) {
    this.levels = levels;
  }

  public boolean isPullDiscsOrdered() {
    return pullDiscsOrdered;
  }

  public void setPullDiscsOrdered(boolean pullDiscsOrdered) {
    this.pullDiscsOrdered = pullDiscsOrdered;
  }

  public int[] getPullDiscs() {
    return pullDiscs;
  }

  public void setPullDiscs(int[] pullDiscs) {
    this.pullDiscs = pullDiscs;
  }

  public int getTurnTime() {
    return turnTime;
  }

  public void setTurnTime(int turnTime) {
    this.turnTime = turnTime;
  }

  public int getVisualizationTime() {
    return visualizationTime;
  }

  public void setVisualizationTime(int visualizationTime) {
    this.visualizationTime = visualizationTime;
  }

  public String getMovePenalty() {
    return movePenalty;
  }

  public void setMovePenalty(String movePenalty) {
    this.movePenalty = movePenalty;
  }
}
