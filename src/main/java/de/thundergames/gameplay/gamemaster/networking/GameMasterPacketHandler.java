/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra 10 2021
 * File created on 15.11.21, 10:24 by Carina
 * Latest changes made by Carina on 15.11.21, 10:22
 * All contents of "GameMasterPacketHandler" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra 10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra 10.
 */

package de.thundergames.gameplay.gamemaster.networking;

import de.thundergames.gameplay.gamemaster.GameMasterClient;
import de.thundergames.networking.client.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import org.jetbrains.annotations.NotNull;

public class GameMasterPacketHandler extends ClientPacketHandler {

  public void handlePacket(@NotNull GameMasterClient client, @NotNull Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("[GameMasterPacketHandler] Packet received: " + packet.getJsonObject());
      // TODO: hier alle PacketHandler eintragen
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DISCONNECT.getPacketType())) {
      System.out.println("[GameMasterPacketHandler] Disconnect received");
      client.getMasterClientThread().disconnect();
    }
  }
}
