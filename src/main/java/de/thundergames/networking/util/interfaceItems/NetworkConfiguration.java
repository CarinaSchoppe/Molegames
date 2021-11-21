/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "NetworkConfiguration" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

import java.util.ArrayList;
import java.util.List;

public class NetworkConfiguration extends InterfaceObject {

  private int playerCount = 0;
  private int radius = 5;
  private int numberOfMoles = 5;
  private ArrayList<NetworkFloor> levels = new ArrayList<>();
  private boolean pullDiscsOrdered = true;
  private final ArrayList<Integer> pullDiscs = new ArrayList<>(List.of(1, 3, 1, 2, 4, 3, 4, 3, 1, 2, 5, 3, 1, 3, 2));
  private int turnTime = 20;
  private int visualizationTime = 10;
  private String movePenalty = "NOTHING";
  private final GameState gameState = new GameState();


  public int getMaxPlayers() {
    return playerCount;
  }

  public void setMaxPlayers(int playerCount) {
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


  public void setLevels(ArrayList<NetworkFloor> levels) {
    this.levels = levels;
  }

  public ArrayList<NetworkFloor> getLevels() {
    return levels;
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
