/*
 *
 *  *     / **
 *  *      *   Copyright Notice                                             *
 *  *      *   Copyright (c) SwtPra10 | ThunderGames 2021                         *
 *  *      *   Created: 05.05.2018 / 11:59                                  *
 *  *      *   All contents of this source text are protected by copyright. *
 *  *      *   The copyright law, unless expressly indicated otherwise, is  *
 *  *      *   at SwtPra10 | ThunderGames. All rights reserved                    *
 *  *      *   Any type of duplication, distribution, rental, sale, award,  *
 *  *      *   Public accessibility or other use                            *
 *  *      *   Requires the express written consent of SwtPra10 | ThunderGames.   *
 *  *      **
 *  *
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
