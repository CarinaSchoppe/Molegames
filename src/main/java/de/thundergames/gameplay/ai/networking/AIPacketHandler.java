package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.networking.client.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import org.jetbrains.annotations.NotNull;

public class AIPacketHandler extends ClientPacketHandler {

  public void handlePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTPLAYER.getPacketType())) {
      if (packet.getValues().getBoolean("move")) {
        ai.setMove(true);
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DRAWNCARD.getPacketType())) {
      ai.setCardValue(true);
    }
  }
}
