/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 23:19 by Carina latest changes made by Carina on 06.12.21, 23:13
 * All contents of "NetworkConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

import java.util.ArrayList;
import java.util.List;

public class NetworkConfiguration {

  private int maxPlayers = 4;
  private int radius = 10;
  private int numberOfMoles = 4;
  private ArrayList<NetworkFloor> levels = new ArrayList<>();
  private boolean pullDiscsOrdered = true;
  private final ArrayList<Integer> pullDiscs = new ArrayList<>(List.of(1, 2, 3, 4));
  private long turnTime = 5000;
  private int visualizationTime = 10;
  private String movePenalty = "NOTHING";
  private final int deductedPoints = 5;

  public int getDeductedPoints() {
    return deductedPoints;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int playerCount) {
    this.maxPlayers = playerCount;
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

  public ArrayList<NetworkFloor> getFloors() {
    return levels;
  }

  public void setFloors(ArrayList<NetworkFloor> levels) {
    this.levels = levels;
  }

  public boolean isPullDiscsOrdered() {
    return pullDiscsOrdered;
  }

  public void setPullDiscsOrdered(boolean pullDiscsOrdered) {
    this.pullDiscsOrdered = pullDiscsOrdered;
  }

  public ArrayList<Integer> getPullDiscs() {
    return pullDiscs;
  }

  public long getTurnTime() {
    return turnTime;
  }

  public void setTurnTime(long turnTime) {
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
