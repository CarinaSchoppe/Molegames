package de.thundergames.network.util;

import de.thundergames.game.util.Game;
import de.thundergames.game.util.GameStates;
import de.thundergames.game.util.MultiGameHandler;
import de.thundergames.game.util.Punishments;
import de.thundergames.network.server.ServerThread;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import de.thundergames.MoleGames;

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

  public static void handlePacket(@NotNull final Packet packet, @NotNull final ServerThread clientConnection) throws IOException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      //    "CREATE-GAME#ID"
      int maxFloors = -1;
      if (!packet.getJsonObject().isNull("maxFloors"))
        maxFloors = packet.getJsonObject().getInt("maxFloors");
      MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(packet.getJsonObject().getInt("punishment")), packet.getJsonObject().getInt("radius"), maxFloors); //<----
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
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
      } else {
        object.put("type", Packets.NOTEXISTS.getPacketType());
        clientConnection.sendPacket(new Packet(object));
      }
    }
  }
}
