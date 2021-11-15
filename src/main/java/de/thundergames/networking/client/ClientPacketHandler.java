/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra 10 2021
 * File created on 15.11.21, 10:24 by Carina
 * Latest changes made by Carina on 15.11.21, 10:22
 * All contents of "ClientPacketHandler" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra 10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra 10.
 */
package de.thundergames.networking.client;

import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.PacketNotExistsException;
import de.thundergames.networking.util.Packets;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

public class ClientPacketHandler {

  /**
   * @param client the client instance that will be passed to the method for handling
   * @param packet the packet that got send by the server
   * @author Carina
   * @use handles the packet that came in
   * @see de.thundergames.networking.util.PacketHandler the packetHandler by the Server as a
   *     reference
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
      try {
        System.out.println("Server sended: " + packet.getJsonObject().getString("message"));
      } catch (JSONException e) {
        System.out.println("Server sended: " + "no packet content!");
      }
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
