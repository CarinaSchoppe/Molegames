/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 15:19 by Carina latest changes made by Carina on 21.11.21, 14:19 All contents of "GameState" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.networking.util.interfaceItems;

import de.thundergames.filehandling.Score;
import java.util.ArrayList;


public class GameState extends InterfaceObject {

  private ArrayList<NetworkPlayer> players = new ArrayList<>();
  private NetworkPlayer currentPlayer;
  private ArrayList<NetworkMole> placedMoles = new ArrayList<>();
  private int moles;
  private int radius;
  private NetworkFloor level;
  private boolean pullDiscsOrdered;
  private ArrayList<Integer> pullDiscs = new ArrayList<>();
  private long visualizationTime;
  private String status;
  private Score score;

  public ArrayList<NetworkPlayer> getPlayers() {
    return players;
  }

  public void setPlayers(ArrayList<NetworkPlayer> players) {
    this.players = players;
  }

  public NetworkPlayer getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(NetworkPlayer currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public ArrayList<NetworkMole> getPlacedMoles() {
    return placedMoles;
  }

  public void setPlacedMoles(ArrayList<NetworkMole> placedMoles) {
    this.placedMoles = placedMoles;
  }

  public int getMoles() {
    return moles;
  }

  public void setMoles(int moles) {
    this.moles = moles;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public NetworkFloor getLevel() {
    return level;
  }

  public void setLevel(NetworkFloor level) {
    this.level = level;
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

  public void setPullDiscs(ArrayList<Integer> pullDiscs) {
    this.pullDiscs = pullDiscs;
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

  public Score getScore() {
    return score;
  }

  public void setScore(Score score) {
    this.score = score;
  }
}
