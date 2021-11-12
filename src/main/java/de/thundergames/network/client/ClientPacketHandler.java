package de.thundergames.network.client;

import de.thundergames.network.util.Packet;
import de.thundergames.network.util.PacketNotExistsException;
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
  public void handlePacket(@NotNull final Client client, @NotNull final Packet packet)
      throws PacketNotExistsException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.LOGIN.getPacketType())) {
      // ID : 3
      var id = packet.getJsonObject().getInt("id");
      client.setId(id);
      System.out.println("Client ID: " + id);
      client.getClientThread().setID(id);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINEDGAME.getPacketType())) {
      System.out.println("Client joined game with id: " + packet.getJsonObject().getInt("gameID"));
      client.setGameID(packet.getJsonObject().getInt("gameID"));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("Server sended: " + packet.getJsonObject().getString("message"));
    } else if (packet.getPacketType().equals(Packets.INVALIDMOVE.getPacketType())) {
      System.out.println("Server: Youve done an invalid move");
    } else if (packet.getPacketType().equals(Packets.PLACEMOLE.getPacketType())) {
    } else if (packet.getPacketType().equals(Packets.MOVEMOLE.getPacketType())) {
    } else if (packet.getPacketType().equals(Packets.MOLES.getPacketType())) {
      for (int i = 0; i < packet.getJsonObject().getJSONArray("moles").toList().size(); i++) {
        client.getMoleIDs().add(packet.getJsonObject().getJSONArray("moles").getInt(i));
      }
    } else {
      throw new PacketNotExistsException(
          "Packet with type: " + packet.getPacketType() + " does not exists");
    }
  }
}
