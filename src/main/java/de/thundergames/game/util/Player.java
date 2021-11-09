package de.thundergames.game.util;

import de.thundergames.network.client.Client;

import java.util.ArrayList;

public class Player {

  private final ArrayList<Mole> moles = new ArrayList<>();
  private int drawCard;
  private final Client client;

  public Player(Client client) {
    this.client = client;
  }

  public void drawAgain(){

  }

  public void drawACard(){

  }

  public void moveMole(final int moleID, final int x, final int y) {

  }

  public ArrayList<Mole> getMoles() {
    return moles;
  }

  public int getDrawCard() {
    return drawCard;
  }

  public Client getClient() {
    return client;
  }
}
