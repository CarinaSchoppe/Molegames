package network.util;

import thundergames.MoleGames;
import game.util.Game;
import game.util.GameStates;
import game.util.MultiGameHandler;
import game.util.Punishments;
import network.server.ServerThread;

import java.io.IOException;

public class PacketHandler {

  /**
   * @author Carina
   * @param packet
   * @param clientConnection
   * @use handles the packets that are comming in and handles it with the client connected
   * @throws IOException
   * @see Packets the packets that can be send-
   */

  public static void handlePacket(Packet packet, ServerThread clientConnection) throws IOException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      //    "CREATE-GAME#ID"
      MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(Integer.parseInt(packet.getPacketContent().toString()))); //<----

    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      //JOIN-GAME#ID
      if (MultiGameHandler.getGames().containsKey(Integer.parseInt(packet.getPacketContent().toString().split("#")[0]))) {
        String connectType = packet.getPacketContent().toString().split("#")[1];
        Game game = MultiGameHandler.getGames().get(Integer.parseInt(packet.getPacketContent().toString().split("#")[0]));
        if (connectType.equalsIgnoreCase("player")) {
          if (game.getCurrentGameState().equals(GameStates.LOBBY))
            game.joinGame(clientConnection, false);
          else
            clientConnection.sendPacket(new Packet(Packets.INGAME.getPacketType(), "null"));
        } else if(connectType.equalsIgnoreCase("spectator"))
          game.joinGame(clientConnection, true);
      } else
        clientConnection.sendPacket(new Packet(Packets.NOTEXISTS.getPacketType(), "null"));
    }
  }
}
