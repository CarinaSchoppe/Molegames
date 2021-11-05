package network.util;

import game.util.Game;
import game.util.GameStates;
import game.util.MultiGameHandler;
import game.util.Punishments;
import network.server.ServerThread;
import org.json.JSONObject;
import thundergames.MoleGames;

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

  public static void handlePacket(Packet packet, ServerThread clientConnection) throws IOException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      //    "CREATE-GAME#ID"
      int maxFloors = -1;
      if (!packet.getJsonObject().isNull("maxFloors"))
        maxFloors = packet.getJsonObject().getInt("maxFloors");
      MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(packet.getJsonObject().getInt("punishment")), packet.getJsonObject().getInt("radius"), maxFloors); //<----
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      JSONObject object = new JSONObject();
      //JOIN-GAME#ID
      if (MultiGameHandler.getGames().containsKey(packet.getJsonObject().getInt("gameID"))) {
        String connectType = packet.getJsonObject().getString("connectType");
        Game game = MultiGameHandler.getGames().get(packet.getJsonObject().getInt("gameID"));
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
