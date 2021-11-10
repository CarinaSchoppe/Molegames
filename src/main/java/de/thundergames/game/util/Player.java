package de.thundergames.game.util;

import de.thundergames.MoleGames;
import de.thundergames.network.server.ServerThread;
import de.thundergames.network.util.Packet;
import de.thundergames.network.util.Packets;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;

public class Player {

  private final ArrayList<Mole> moles = new ArrayList<>();
  private int drawCard = 0;
  private final ServerThread serverClient;
  private Timer timer;
  private boolean canDraw = false;
  private final Game game;
  private boolean hasMoved = true;
  private List<Integer> cards;

  public Player(@NotNull final  ServerThread client,@NotNull final  Game game) {
    this.serverClient = client;
    this.game = game;
    this.cards = new ArrayList<>(game.getSettings().getCards());
  }

  public synchronized Player create() {
    var moleIDs = new ArrayList<Integer>();
    for (int i = 0; i < game.getSettings().getMoleAmount(); i++) {
      var mole = new Mole(game.getMoleID());
      moles.add(mole);
      game.setMoleID(game.getMoleID() + 1);
      game.getMoleMap().put(this, mole);
      moleIDs.add(game.getMoleID());
      game.getMoleIDMap().put(mole.getMoleID(), mole);
    }
    MoleGames.getMoleGames().getPacketHandler().sendMoleIDs(serverClient, moleIDs);
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
    }, game.getSettings().getTimeToThink() * 1000L);
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
      cards = new ArrayList<>(game.getSettings().getCards());
    if (game.getSettings().isRandomDraw()) {
      drawCard = cards.get(new Random().nextInt(cards.size()));
    } else {
      drawCard = cards.get(0);
    }
    cards.remove(drawCard);
    MoleGames.getMoleGames().getPacketHandler().drawnPlayerCardPacket(serverClient, drawCard);
  }

  public void moveMole(final int moleID, final int x_start, final int y_start, final int x_end, final int y_end) {
    if (!game.getCurrentPlayer().equals(this) || hasMoved || getMole(moleID) == null)
      return;
    if (MoleGames.getMoleGames().getGameLogic().wasLegalMove(List.of(x_start, y_start), List.of(x_end, y_end), 3, game.getMap())) { //TODO: drawCard - 3
      Objects.requireNonNull(getMole(moleID)).setField(game.getMap().getFloor().getFieldMap().get(List.of(x_start, y_start)));
      game.getMap().getFloor().getOccupied().remove(game.getMap().getFloor().getFieldMap().get(List.of(x_start, y_start)));
      game.getMap().getFloor().getOccupied().add(game.getMap().getFloor().getFieldMap().get(List.of(x_end, y_end)));
      game.getMap().getFloor().getFieldMap().get(List.of(x_start, y_start)).setOccupied(false);
      game.getMap().getFloor().getFieldMap().get(List.of(x_end, y_end)).setOccupied(true);
      MoleGames.getMoleGames().getServer().sendToGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerMovesMolePacket(moleID, x_end, y_end));
      hasMoved = true;
      System.out.println("Player with id: " + serverClient.getConnectionId() + " has moved his mole from: x=" + x_start + " y=" + y_start + " to x=" + x_end + " y=" + y_end + ".");
      timer.cancel();
      timer.purge();
      game.nextPlayer();
    } else {
      System.out.println("Client with id: " + serverClient.getConnectionId() + " has done in invalid move Punishment: " + game.getSettings().getPunishment());
      serverClient.sendPacket(MoleGames.getMoleGames().getPacketHandler().invalidMovePacket(serverClient));
    }
  }

  public void placeMole(final int x, final int y, final int moleID) {
    if (!game.getCurrentPlayer().equals(this) || hasMoved || getMole(moleID) == null)
      return;
    if (game.getMap().getFloor().getFieldMap().get(List.of(x, y)).isOccupied() || game.getMap().getFloor().getFieldMap().get(List.of(x, y)).isHole()) {
      serverClient.sendPacket(new Packet(new JSONObject().put("type", Packets.OCCUPIED.getPacketType())));
    } else {
      Objects.requireNonNull(getMole(moleID)).setField(game.getMap().getFloor().getFieldMap().get(List.of(x, y)));
      game.getMap().getFloor().getOccupied().add(game.getMap().getFloor().getFieldMap().get(List.of(x, y)));
      game.getMap().getFloor().getFieldMap().get(List.of(x, y)).setOccupied(true);
      MoleGames.getMoleGames().getServer().sendToGameClients(game, MoleGames.getMoleGames().getPacketHandler().playerPlacesMolePacket(moleID, x, y));
      game.getMap().printMap();
      hasMoved = true;
      timer.cancel();
      timer.purge();
      game.nextPlayer();
      System.out.println("Player with id: " + serverClient.getConnectionId() + " has placed his mole on x=" + x + " y=" + y + ".");
    }
  }

  private Mole getMole(final int moleID) {
    Mole mole;
    for (Mole value : moles) {
      if (value.getMoleID() == moleID) {
        mole = value;
        return mole;
      }
    }
    return null;
  }

  public Game getGame() {
    return game;
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
