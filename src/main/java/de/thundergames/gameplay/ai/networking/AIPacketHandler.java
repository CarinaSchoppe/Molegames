package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.networking.client.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import org.jetbrains.annotations.NotNull;

public class AIPacketHandler extends ClientPacketHandler {

  public void handlePacket(@NotNull AI AI, @NotNull Packet packet) {
    if (packet.getPacketType().equals("AI")) {
      if (packet.getValues().getBoolean("move")) {
        AI.setMove(true);
      }
    }
  }
}
