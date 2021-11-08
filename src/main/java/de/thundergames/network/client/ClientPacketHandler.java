package de.thundergames.network.client;

import de.thundergames.network.util.Packet;
import de.thundergames.network.util.Packets;
import org.jetbrains.annotations.NotNull;

public class ClientPacketHandler {

  /**
   * @param client the client instance that will be passed to the method for handling
   * @param packet the packet that got send by the server
   * @author Carina
   * @use handles the packet that came in
   * @see de.thundergames.network.util.PacketHandler the packetHandler by the Server as a reference
   */
  public void handlePacket(Client client, @NotNull Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.GIVEID.getPacketType())) {
      //ID : 3
      var id = packet.getJsonObject().getInt("id");
      client.setId(id);
      System.out.println("Client ID: " + id);
      client.getClientThread().setID(id);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINEDGAME.getPacketType())) {
      System.out.println("Client joined game with id: " + packet.getJsonObject().getInt("gameID"));
      client.setGameID(packet.getJsonObject().getInt("gameID"));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("Server sended: " + packet.getJsonObject().getString("message"));
    }
  }
}
