package de.thundergames.game.util;

import de.thundergames.network.server.ServerThread;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Player {

  private final ArrayList<Mole> moles = new ArrayList<>();
  private int drawCard;
  private final ServerThread serverClient;
  private Timer timer = new Timer();
  private boolean canDraw = false;
  private Game game;

  public Player(ServerThread client, Game game) {
    this.serverClient = client;
    this.game = game;
  }

  private void startThinkTimer() {
    timer = new Timer();
    canDraw = true;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        canDraw = false;
      }
    }, game.getTimeToThink());
  }

  public void drawACard() {
    if (game.isRandomDraw()) {
    } else {
    }
  }

  public void moveMole(final int moleID, final int x, final int y) {
  }

  public ArrayList<Mole> getMoles() {
    return moles;
  }

  public int getDrawCard() {
    return drawCard;
  }

  public ServerThread getServerClient() {
    return serverClient;
  }
}
