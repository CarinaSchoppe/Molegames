/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 15:51 by Carina
 * Latest changes made by Carina on 15.11.21, 15:43
 * All contents of "GameMasterPacketHandler" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.networking;

import de.thundergames.gameplay.ausrichter.GameMasterClient;
import de.thundergames.networking.client.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import org.jetbrains.annotations.NotNull;

public class GameMasterPacketHandler extends ClientPacketHandler {

  public void handlePacket(@NotNull GameMasterClient client, @NotNull Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("[GameMasterPacketHandler] Packet received: " + packet.getValues());
      // TODO: hier alle PacketHandler eintragen
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DISCONNECT.getPacketType())) {
      System.out.println("[GameMasterPacketHandler] Disconnect received");
      client.getMasterClientThread().disconnect();
    }
  }
}
