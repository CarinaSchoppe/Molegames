package de.thundergames.game.util;

import de.thundergames.MoleGames;
import de.thundergames.network.server.ServerThread;
import de.thundergames.network.util.Packet;
import de.thundergames.network.util.Packets;
import org.json.JSONObject;

import java.util.*;

public class Player {

  private final ArrayList<Mole> moles = new ArrayList<>();
  private int drawCard = 0;
  private final ServerThread serverClient;
  private Timer timer;
  private boolean canDraw = false;
  private Game game;
  private boolean hasMoved = true;
  private List<Integer> cards;

  public Player(ServerThread client, Game game) {
    this.serverClient = client;
    this.game = game;
    this.cards = (List<Integer>) game.getSettings().getCards().clone();
  }

  public synchronized Player create() {
    for (int i = 0; i < game.getSettings().getMoleAmount(); i++) {
      var mole = new Mole(game.getMoleID());
      moles.add(mole);
      game.setMoleID(game.getMoleID() + 1);
      game.getMoleMap().put(this, mole);
      game.getMoleIDMap().put(mole.getMoleID(), mole);
    }
    return this;
  }

  public void startThinkTimer() {
    hasMoved = false;
    timer = new Timer();
    canDraw = true;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        canDraw = false;
        hasMoved = true;
      }
    }, game.getSettings().getTimeToThink() * 1000);
  }

  /**
   * @return the canDraw
   * @author carina
   * @use when called draws a card and returns it depending on
   * @see Settings.randomDraw if the card should be taken in order or randomly if cards a empty refill by the oder
   */
  public void drawACard() {
    if (!game.getCurrentPlayer().equals(this) || hasMoved || !canDraw)
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
    if (!game.getCurrentPlayer().equals(this) || hasMoved)
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
    if (MoleGames.getMoleGames().getGameLogic().wasLegalMove(Collections.unmodifiableList(Arrays.asList(x_start, y_start)), Collections.unmodifiableList(Arrays.asList(x_end, y_end)), drawCard, game.getMap())) {
      game.getMap().getFloor().getOccupied().remove(game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x_start, y_start))));
      game.getMap().getFloor().getOccupied().add(game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x_end, y_end))));
      game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x_start, y_start))).setOccupied(false);
      game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x_end, y_end))).setOccupied(true);
      MoleGames.getMoleGames().getServer().sendToGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerMovesMolePacket(moleID, x_end, y_end));
      hasMoved = true;
      timer.cancel();
      timer.purge();
      game.nextPlayer();
    } else
      MoleGames.getMoleGames().getPacketHandler().invalidMovePacket(serverClient);
  }

  public void placeMole(final int x, final int y, final int moleID) {
    if (!game.getCurrentPlayer().equals(this))
      return;
    if (hasMoved)
      return;
    if (game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x, y))).isOccupied() || game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x, y))).isHole()) {
      serverClient.sendPacket(new Packet(new JSONObject().put("type", Packets.OCCUPIED.getPacketType())));
      System.out.println("hierrr");
    } else {
      Mole mole = null;
      for (int i = 0; i < moles.size(); i++) {
        if (moles.get(i).getMoleID() == moleID) {
          mole = moles.get(i);
          break;
        }
      }
      if (mole == null)
        return;
      game.getMap().getFloor().getOccupied().add(game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x, y))));
      game.getMap().getFloor().getFieldMap().get(Collections.unmodifiableList(Arrays.asList(x, y))).setOccupied(true);
      MoleGames.getMoleGames().getServer().sendToGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerPlacesMolePacket(moleID, x, y));
      game.getMap().printMap();
      hasMoved = true;
      timer.cancel();
      timer.purge();
      game.nextPlayer();

    }
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
