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
import java.util.ArrayList;

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
      updateConfigurationPacket(packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("MESSAGE: Client with id: " + clientConnection.id + " sended: type: " + packet.getPacketType() + " contents: " + packet.getJsonObject().toString());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMESTART.getPacketType())) {
      startGamePacket(packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NAME.getPacketType())) {
      clientConnection.setClientName(packet.getJsonObject().getString("name"));
      System.out.println("Client with id: " + clientConnection.id + " got the name:" + packet.getJsonObject().getString("name"));
    } else {
      throw new PacketNotExistsException("Packet not exists");
    }
  }

  private synchronized void startGamePacket(@NotNull final Packet packet) {
    if (MoleGames.getMoleGames().getGameHandler().getGames().containsKey(packet.getJsonObject().getInt("gameID")))
      MoleGames.getMoleGames().getGameHandler().getGames().get(packet.getJsonObject().getInt("gameID")).start();
  }

  private synchronized void updateConfigurationPacket(@NotNull final Packet packet) {
    Game game = MoleGames.getMoleGames().getGameHandler().getGames().get(packet.getJsonObject().getInt("gameID"));
    if (game != null)
      game.getSettings().updateConfiuration(packet.getJsonObject());
  }

  private synchronized void nextPlayerPacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    var game = MoleGames.getMoleGames().getGameHandler().getClientGames().get(packet.getJsonObject().getInt("gameID"));
    game.nextPlayer();
  }

  public synchronized void drawnPlayerCardPacket(@NotNull final ServerThread clientConnection, final int cardID) {
    var object = new JSONObject();
    object.put("type", Packets.DRAWNCARD.getPacketType());
    object.put("card", cardID);
    clientConnection.sendPacket(new Packet(object));
  }

  private synchronized void drawPlayerCardPacket(@NotNull final ServerThread clientConnection) {
    var player = MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getClientPlayersMap().get(clientConnection);
    player.drawACard();
  }

  public synchronized Packet playerPlacesMolePacket(final int moleID, final int x, final int y) {
    var object = new JSONObject();
    object.put("type", Packets.PLACEMOLE.getPacketType());
    object.put("moleID", moleID);
    object.put("x", x);
    object.put("y", y);
    return new Packet(object);
  }

  public synchronized Packet playerMovesMolePacket(final int moleID, final int x, final int y) {
    var object = new JSONObject();
    object.put("type", Packets.MOVEMOLE.getPacketType());
    object.put("moleID", moleID);
    object.put("x", x);
    object.put("y", y);
    return new Packet(object);
  }

  public synchronized Packet gameOverPacket() {
    var object = new JSONObject();
    object.put("type", Packets.GAMEOVER.getPacketType());
    return new Packet(object);
  }

  public synchronized Packet invalidMovePacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.INVALIDMOVE.getPacketType());
    return new Packet(object);
  }

  public synchronized Packet playerKickedPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.PLAYERKICKED.getPacketType());
    object.put("id", clientConnection.id);
    return new Packet(object);
  }

  public synchronized Packet nextFloorPacket(@NotNull final Map map) {
    var object = new JSONObject();
    object.put("type", Packets.NEXTFLOOR.getPacketType());
    object.put("floor", map.getFloor());
    return new Packet(object);
  }

  public synchronized void playerTurnPacket(@NotNull final ServerThread clientConnection) {
    var object = new JSONObject();
    object.put("type", Packets.PLAYERTURN.getPacketType());
    clientConnection.sendPacket(new Packet(object));
  }

  public synchronized void sendMoleIDs(@NotNull final ServerThread clientConnection, ArrayList<Integer> moleIDs) {
    var object = new JSONObject();
    object.put("type", Packets.MOLES.getPacketType());
    object.put("moles", moleIDs);
    clientConnection.sendPacket(new Packet(object));
  }

  private synchronized void leaveGamePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }

  private synchronized void timeToThinkPacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
  }

  public synchronized Packet startGamePacket() {
    return new Packet(new JSONObject().put("type", Packets.GAMESTART.getPacketType()));
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
    var position = MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getMoleIDMap().get(packet.getJsonObject().getInt("moleID")).getField();
    MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getClientPlayersMap().get(clientConnection).moveMole(
      packet.getJsonObject().getInt("moleID"),
      position.getX(),
      position.getY(),
      packet.getJsonObject().getInt("x"),
      packet.getJsonObject().getInt("y"));
  }

  private synchronized void placeMolePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) {
    MoleGames.getMoleGames().getGameHandler().getClientGames().get(clientConnection).getClientPlayersMap().get(clientConnection).placeMole(packet.getJsonObject().getInt("x"), packet.getJsonObject().getInt("y"), packet.getJsonObject().getInt("moleID"));
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
        if (game.getCurrentGameState().equals(GameStates.LOBBY)) {
          if (game.getClients().size() < game.getSettings().getMaxPlayers()) {
            game.joinGame(new Player(clientConnection, game).create(), false);
          } else {
            object.put("type", Packets.FULL.getPacketType());
            clientConnection.sendPacket(new Packet(object));
          }
        } else {
          object.put("type", Packets.INGAME.getPacketType());
          clientConnection.sendPacket(new Packet(object));
        }
      } else if (connectType.equalsIgnoreCase("spectator")) {
        if (!game.getCurrentGameState().equals(GameStates.RESETSTATE) || !game.getCurrentGameState().equals(GameStates.WINNINGSTATE)) {
          game.joinGame(new Player(clientConnection, game).create(), true);
        }
      }
    } else {
      object.put("type", Packets.NOTEXISTS.getPacketType());
      clientConnection.sendPacket(new Packet(object));
    }
  }

  private synchronized void createGamePacket() {
    //    "CREATE-GAME#ID"
    MoleGames.getMoleGames().getGameHandler().createNewGame();
  }
}
