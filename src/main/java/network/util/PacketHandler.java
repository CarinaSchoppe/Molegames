package network.util;

import blitzgames.MoleGames;
import game.util.Game;
import game.util.GameStates;
import game.util.MultiGameHandler;
import game.util.Punishments;
import network.server.ServerThread;

import java.io.IOException;

public class PacketHandler {

  public static void handlePacket(Packet packet, ServerThread clientConnection) throws IOException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.CREATEGAME.getPacketType())) {
      //    "CREATE-GAME#ID"
      MoleGames.getMoleGames().getGameHandler().createNewGame(Punishments.getPunishmentByID(Integer.parseInt(packet.getPacketContent().toString())));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      //JOIN-GAME#ID
      if (MultiGameHandler.getGames().containsKey(Integer.parseInt(packet.getPacketContent().toString().split("#")[0]))) {
        String connectType = packet.getPacketContent().toString().split("#")[1];
        Game game = MultiGameHandler.getGames().get(Integer.parseInt(packet.getPacketContent().toString()));
        if (connectType.equalsIgnoreCase("player")) {
          if (game.getCurrentGameState().equals(GameStates.LOBBY))
            game.joinGame(clientConnection, false);
          else
            clientConnection.sendPacket(new Packet(Packets.INGAME.getPacketType(), "null"));
        } else
          game.joinGame(clientConnection, true);
      } else
        clientConnection.sendPacket(new Packet(Packets.NOTEXISTS.getPacketType(), "null"));
    }
  }
}
