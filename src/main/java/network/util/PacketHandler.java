package network.util;

import blitzgames.MoleGames;
import game.util.MultiGameHandler;
import game.util.Punishments;
import network.server.ServerThread;

import java.io.IOException;

public class PacketHandler {

  public static void handlePacket(Packet packet, NetworkThread clientConnection) throws IOException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      //    "CREATE-GAME#ID"
      MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(Integer.parseInt(packet.getPacketContent().toString())));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      //JOIN-GAME#ID
      MultiGameHandler.getGames().get(Integer.parseInt(packet.getPacketContent().toString())).joinGame((ServerThread) clientConnection);
    }
  }
}
