package network.util;

import blitzgames.MoleGames;
import game.util.MultiGameHandler;
import game.util.Punishments;
import network.server.ServerThread;

public class PacketHandler {

  public static void handlePacket(Packet packet, NetworkThread clientConnection) {
    if (packet.getPacketContent().split("#")[0].equalsIgnoreCase(Packets.CREATEGAME.getContent())) {
      //    "CREATE-GAME#ID"
      MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(Integer.parseInt(packet.getPacketContent().split("#")[1])));
    } else if (packet.getPacketContent().split("#")[0].equalsIgnoreCase(Packets.JOINGAME.getContent())) {
      //JOIN-GAME#ID
      MultiGameHandler.getGames().get(Integer.parseInt(packet.getPacketContent().split("#")[1])).joinGame((ServerThread) clientConnection);
    }
  }
}
