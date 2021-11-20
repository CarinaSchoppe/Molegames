package de.thundergames.playmechanics.util.interfaceItems;

import de.thundergames.filehandling.Score;

public class NetworkGame  extends InterfaceObject{

  private final int GameID;
  private int currentPlayerCount;
  private int maxPlayerCount;
  private int levelCount;
  private int moleCount;
  private int radius;
  private boolean pullDiscsOrdered;
  private int[] pullDiscs;
  private long turnTime;
  private long visualizationTime;
  private String status;
  private String movePenalty;
  private long startDateTime;
  private long finishDateTime;
  private Score result;

  public NetworkGame(int gameID) {
    GameID = gameID;
  }

  public int getGameID() {
    return GameID;
  }

  public int getCurrentPlayerCount() {
    return currentPlayerCount;
  }

  public void setCurrentPlayerCount(int currentPlayerCount) {
    this.currentPlayerCount = currentPlayerCount;
  }

  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }

  public void setMaxPlayerCount(int maxPlayerCount) {
    this.maxPlayerCount = maxPlayerCount;
  }

  public int getLevelCount() {
    return levelCount;
  }

  public void setLevelCount(int levelCount) {
    this.levelCount = levelCount;
  }

  public int getMoleCount() {
    return moleCount;
  }

  public void setMoleCount(int moleCount) {
    this.moleCount = moleCount;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
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

  public long getTurnTime() {
    return turnTime;
  }

  public void setTurnTime(long turnTime) {
    this.turnTime = turnTime;
  }

  public long getVisualizationTime() {
    return visualizationTime;
  }

  public void setVisualizationTime(long visualizationTime) {
    this.visualizationTime = visualizationTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMovePenalty() {
    return movePenalty;
  }

  public void setMovePenalty(String movePenalty) {
    this.movePenalty = movePenalty;
  }

  public long getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(long startDateTime) {
    this.startDateTime = startDateTime;
  }

  public Score getResult() {
    return result;
  }

  public void setResult(Score result) {
    this.result = result;
  }

  public long getFinishDateTime() {
    return finishDateTime;
  }

  public void setFinishDateTime(long finishDateTime) {
    this.finishDateTime = finishDateTime;
  }
}
