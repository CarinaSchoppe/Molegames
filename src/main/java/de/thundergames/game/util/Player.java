package de.thundergames.game.util;

import de.thundergames.MoleGames;
import de.thundergames.network.server.ServerThread;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Player {

  private final ArrayList<Mole> moles = new ArrayList<>();
  private int drawCard = 0;
  private final ServerThread serverClient;
  private Timer timer = new Timer();
  private boolean canDraw = false;
  private Game game;
  private ArrayList<Integer> cards;

  public Player(ServerThread client, Game game) {
    this.serverClient = client;
    this.game = game;
    this.cards = (ArrayList<Integer>) game.getSettings().getCards().clone();
  }

  private void startThinkTimer() {
    timer = new Timer();
    canDraw = true;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        canDraw = false;
      }
    }, game.getSettings().getTimeToThink());
  }

  /**
   * @return the canDraw
   * @author carina
   * @use when called draws a card and returns it depending on
   * @see Settings.randomDraw if the card should be taken in order or randomly if cards a empty refill by the oder
   */
  public void drawACard() {
    if (!canDraw)
      return;
    if (cards.size() == 0)
      cards = (ArrayList<Integer>) game.getSettings().getCards().clone();
    if (game.getSettings().isRandomDraw()) {
      drawCard = cards.get(new Random().nextInt(cards.size()));
      cards.remove(drawCard);
    } else {
      drawCard = cards.get(0);
      cards.remove(drawCard);
    }
    MoleGames.getMoleGames().getPacketHandler().drawnPlayerCardPacket(serverClient, drawCard);
  }

  public void moveMole(final int moleID, final int x_start, final int y_start, final int x_end, final int y_end) {
    if (!game.getCurrentPlayer().equals(this))
      return;
    Mole mole = null;
    for (int i = 0; i < moles.size(); i++) {
      if (moles.get(i).getMoleID() == moleID) {
        mole = moles.get(i);
        break;
      }
    }
    if (mole == null)
      return;
    if (MoleGames.getMoleGames().getGameLogic().wasLegalMove(new int[]{x_start, y_start}, new int[]{x_end, y_end}, drawCard, game.getMap()))
      MoleGames.getMoleGames().getServer().sendToGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerMovesMolePacket(moleID, x_end, y_end));
    else
      MoleGames.getMoleGames().getPacketHandler().invalidMovePacket(serverClient);
  }

  public void placeMole(final int moleID, final int x, final int y) {
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
