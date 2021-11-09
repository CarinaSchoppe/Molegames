package de.thundergames.network.util;

import de.thundergames.MoleGames;
import de.thundergames.game.map.Floors;
import de.thundergames.game.map.Map;
import de.thundergames.game.util.GameStates;
import de.thundergames.game.util.MultiGameHandler;
import de.thundergames.game.util.Player;
import de.thundergames.game.util.Punishments;
import de.thundergames.network.client.Client;
import de.thundergames.network.server.ServerThread;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

public class PacketHandler {

  /**
   * @param packet
   * @param clientConnection
   * @throws IOException
   * @author Carina
   * @use handles the packets that are comming in and handles it with the client connected
   * @see Packets the packets that can be send-
   */

  public synchronized void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      createGamePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      joinGamePacket(packet, clientConnection);
    }else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEOVERVIEW.getPacketType())) {
      gameOverviewPacket(packet, clientConnection);
    }else if (packet.getPacketType().equalsIgnoreCase(Packets.PLACEMOLE.getPacketType())) {
      placeMolePacket(packet, clientConnection);
    }else if (packet.getPacketType().equalsIgnoreCase(Packets.MOVEMOLE.getPacketType())) {
      moveMolePacket(packet, clientConnection);
    }else if(packet.getPacketType().equalsIgnoreCase(Packets.TIMETOTHINK.getPacketType())){
      timeToThinkPacket(packet, clientConnection);
    }else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVEGAME.getPacketType())) {
      leaveGamePacket(packet, clientConnection);
    }

  }


  public void drawPlayerCardPacket(@NotNull final ServerThread clientConnection, final int number) {
    var object = new JSONObject();
    object.put("type", Packets.DRAWCARD.getPacketType());
    object.put("card", number);
    clientConnection.sendPacket(new Packet(object));
  }

  public synchronized void playerPlacesMolePacket(@NotNull final ServerThread clientConnection, final int moleID, final int x, final int y) {
    var object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    object.put("moleID", moleID);
    object.put("x", x);
    object.put("y", y);
    clientConnection.sendPacket(new Packet(object));
  }

  public void playerMovesMolePacket(@NotNull final ServerThread clientConnection, final int moleID, final int x, final int y) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    object.put("moleID", moleID);
    object.put("x", x);
    object.put("y", y);
    clientConnection.sendPacket(new Packet(object));
  }

  public void invalidMovePacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.INVALIDMOVE.getPacketType());
    clientConnection.sendPacket(new Packet(object));
  }

  public synchronized void playerKickedPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.PLAYERKICKED.getPacketType());
    object.put("id", clientConnection.id);
    clientConnection.sendPacket(new Packet(object));
  }

  public void nextFloorPacket(@NotNull final ServerThread clientConnection, @NotNull final Map map) {
    var object = new JSONObject();
    object.put("type", Packets.NEXTFLOOR.getPacketType());
    object.put("floor", map.getFloor());
    clientConnection.sendPacket(new Packet(object));
  }

  public synchronized void playerSuspendsPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.PLAYERSUSPENDS.getPacketType());
    clientConnection.sendPacket(new Packet(object));
  }

  public synchronized void playerTurnPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.PLAYERTURN.getPacketType());
    clientConnection.sendPacket(new Packet(object));
  }

  private synchronized void leaveGamePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {

  }

  private synchronized void timeToThinkPacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {

  }


  public synchronized void startGamePacket(@NotNull final ServerThread clientConnection) {

  }

  public synchronized void joinedGamePacket( @NotNull final ServerThread clientConnection, final int gameID) {
    var object = new JSONObject();
    object.put("type", Packets.JOINEDGAME.getPacketType());
    object.put("gameID", gameID);
    clientConnection.sendPacket(new Packet(object));
  }



  public void loginPacket(@NotNull final ServerThread clientConnection, final int threadID) {
    var object = new JSONObject();
    object.put("type", Packets.LOGIN.getPacketType());
    object.put("id", threadID);
    clientConnection.sendPacket(new Packet(object));
  }

  private synchronized void moveMolePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {

  }

  private synchronized void placeMolePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {

  }

  private synchronized void gameOverviewPacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }

  private synchronized void joinGamePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    //JOIN-GAME#ID
    if (MultiGameHandler.getGames().containsKey(packet.getJsonObject().getInt("gameID"))) {
      var connectType = packet.getJsonObject().getString("connectType");
      var game = MultiGameHandler.getGames().get(packet.getJsonObject().getInt("gameID"));
      if (connectType.equalsIgnoreCase("player")) {
        if (game.getCurrentGameState().equals(GameStates.LOBBY))
          game.joinGame(clientConnection, false);
        else {
          object.put("type", Packets.INGAME.getPacketType());
          clientConnection.sendPacket(new Packet(object));
        }
      } else if (connectType.equalsIgnoreCase("spectator"))
        game.joinGame(clientConnection, true);
      else if (connectType.equalsIgnoreCase("player"))
        game.joinGame(clientConnection, false);
    } else {
      object.put("type", Packets.NOTEXISTS.getPacketType());
      clientConnection.sendPacket(new Packet(object));
    }
  }

  private synchronized void createGamePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    //    "CREATE-GAME#ID"
    int maxFloors = -1;
    if (!packet.getJsonObject().isNull("maxFloors"))
      maxFloors = packet.getJsonObject().getInt("maxFloors");
    MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(packet.getJsonObject().getInt("punishment")), packet.getJsonObject().getInt("radius"), maxFloors); //<----
  }
}
