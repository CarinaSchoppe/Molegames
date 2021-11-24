/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 13:45 by Carina latest changes made by Carina on 23.11.21, 13:45 All contents of "AIPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.networking.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.networking.util.exceptions.UndefinedError;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import org.jetbrains.annotations.NotNull;

public class AIPacketHandler extends ClientPacketHandler {


  /**
   * @param ai     the instance of the AI
   * @param packet the packet recieved
   * @throws UndefinedError
   * @author Carina
   * @use the logic for the AI to decide what to do depending on the packet recieved
   */
  public void handlePacket(@NotNull final AI ai, @NotNull final Packet packet) throws UndefinedError {
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      if (!packet.getValues().get("magic").getAsString().equals("mole42")) {
        System.exit(3);
      }
      ai.getClientThread().setID(packet.getValues().get("clientID").getAsInt());
      ai.setNetworkPlayer(new NetworkPlayer(ai.getName(), ai.getClientThread().getClientThreadID()));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOMEGAME.getPacketType())) {
      handleWelcomeGamePacket(ai, packet);
      ai.getAIUtil().createMapFromJson(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTLEVEL.getPacketType())) {
      handleNextFloorPacket(ai, packet);
      ai.getAIUtil().createMapFromJson(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEPLACED.getPacketType())) {
      handleMolePlacedPacket(ai, packet);
      ai.getAIUtil().createMapFromJson(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEMOVED.getPacketType())) {
      handleMoleMovedPacket(ai, packet);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSTURN.getPacketType())) {
      handlePlayersTurnPacket(ai, packet);
      try {
        Thread.sleep(100);
        ai.getLogic().handleAction(ai);
        if (isTimerRunning()) {
          timer.purge();
          timer.cancel();
          setTimerRunning(false);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERPLACESMOLE.getPacketType())) {
      handlePlayerPlacesMolePacket(ai, packet);
      try {
        Thread.sleep(100);
        ai.getLogic().handleAction(ai);
        if (isTimerRunning()) {
          timer.purge();
          timer.cancel();
          setTimerRunning(false);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }


}
