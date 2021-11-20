package de.thundergames.playmechanics.util;

import de.thundergames.networking.util.interfaceItems.NetworkGame;

public class Tournament {

  private final int tournamentID;
  private int playerCount;
  private NetworkGame[] games;

  public Tournament(int tournamentID) {
    this.tournamentID = tournamentID;
  }


  public void create() {
  }
}
