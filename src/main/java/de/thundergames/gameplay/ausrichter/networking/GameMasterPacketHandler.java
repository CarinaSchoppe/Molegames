/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.11.21, 10:33 by Carina Latest changes made by Carina on 18.11.21, 10:32
 * All contents of "GameMasterPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.networking;

import de.thundergames.gameplay.ausrichter.GameMasterClient;
import de.thundergames.gameplay.player.networking.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import org.jetbrains.annotations.NotNull;

public class GameMasterPacketHandler extends ClientPacketHandler {

  //TODO: aufteilen in einzelne Methoden

  /**
   * @param client
   * @param packet
   * @author Carina
   * @use handles the packets incomming for a GameMasterClient
   */
  public void handlePacket(@NotNull GameMasterClient client, @NotNull Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (!packet.getValues().isEmpty() && !packet.getValues().toString().equalsIgnoreCase("{}")) {
        System.out.println("[GameMasterPacketHandler] Packet received: " + packet.getValues());
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DISCONNECT.getPacketType())) {
      System.out.println("[GameMasterPacketHandler] Disconnect received");
      client.getMasterClientThread().disconnect();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      welcomePacket(client, packet);
    }
  }
}
