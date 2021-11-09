package de.thundergames.network.util;

import de.thundergames.MoleGames;
import de.thundergames.game.map.Map;
import de.thundergames.game.util.Game;
import de.thundergames.game.util.GameStates;
import de.thundergames.game.util.Player;
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

  public synchronized void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) throws PacketNotExistsException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      createGamePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      joinGamePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEOVERVIEW.getPacketType())) {
      gameOverviewPacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLACEMOLE.getPacketType())) {
      placeMolePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOVEMOLE.getPacketType())) {
      moveMolePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.TIMETOTHINK.getPacketType())) {
      timeToThinkPacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.LEAVEGAME.getPacketType())) {
      leaveGamePacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTPLAYER.getPacketType())) {
      nextPlayerPacket(packet, clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DRAWCARD.getPacketType())) {
      drawPlayerCardPacket(clientConnection);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.CONFIGURATION.getPacketType())) {
    } else {
      throw new PacketNotExistsException("Packet not exists");
    }
  }

  private void updateConfigurationPacket(@NotNull final Packet packet) {
    Game game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(packet.getJsonObject().getInt("gameID"));
    game.getSettings().updateConfiuration(packet.getJsonObject());
  }

  private synchronized void nextPlayerPacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }

  public synchronized void drawnPlayerCardPacket(@NotNull final ServerThread clientConnection, final int cardID) {
    var object = new JSONObject();
    object.put("type", Packets.DRAWNCARD.getPacketType());
    object.put("cardID", cardID);
    clientConnection.sendPacket(new Packet(object));
  }

  private void drawPlayerCardPacket(@NotNull final ServerThread clientConnection) {
    var player = MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getClientPlayersMap().get(clientConnection);
    player.drawACard();
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

  public synchronized void gameOverPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.GAMEOVER.getPacketType());
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

  public synchronized void joinedGamePacket(@NotNull final ServerThread clientConnection, final int gameID) {
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
    if (MoleGames.getMoleGames().getGameHandler().getGames().containsKey(packet.getJsonObject().getInt("gameID"))) {
      var connectType = packet.getJsonObject().getString("connectType");
      var game = MoleGames.getMoleGames().getGameHandler().getGames().get(packet.getJsonObject().getInt("gameID"));
      if (connectType.equalsIgnoreCase("player")) {
        if (game.getCurrentGameState().equals(GameStates.LOBBY))
          game.joinGame(new Player(clientConnection, game), false);
        else {
          object.put("type", Packets.INGAME.getPacketType());
          clientConnection.sendPacket(new Packet(object));
        }
      } else if (connectType.equalsIgnoreCase("spectator"))
        game.joinGame(new Player(clientConnection, game), true);
      else if (connectType.equalsIgnoreCase("player"))
        game.joinGame(new Player(clientConnection, game), false);
    } else {
      object.put("type", Packets.NOTEXISTS.getPacketType());
      clientConnection.sendPacket(new Packet(object));
    }
  }

  private synchronized void createGamePacket() {
    //    "CREATE-GAME#ID"
    MoleGames.getMoleGames().getGameHandler().createNewGame(); //<----
  }
}
