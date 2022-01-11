/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 11.01.22, 20:01 by Carina Latest changes made by Carina on 11.01.22, 18:14 All contents of "AIPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.networking.ClientPacketHandler;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.Packets;
import de.thundergames.playmechanics.util.Player;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class AIPacketHandler extends ClientPacketHandler {

  public AIPacketHandler(Client client) {
    super(client);
  }

  /**
   * @param ai     the instance of the AI
   * @param packet the packet received
   * @author Carina
   * @use the logic for the AI to decide what to do depending on the packet received
   */
  public void handlePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    this.packet = packet;
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      if (!packet.getValues().get("magic").getAsString().equals("mole42")) {
        System.exit(3);
      }
      ai.getClientThread().setThreadID(packet.getValues().get("clientID").getAsInt());
      ai.setPlayer(new Player(ai));
      loginPacket(ai.getName());
      if (ai.getGameID() != -1) {
        joinGamePacket(ai.getGameID(), true);
      }
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOMEGAME.getPacketType())) {
      handleWelcomeGamePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTLEVEL.getPacketType())) {
      handleNextFloorPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEPLACED.getPacketType())) {
      handleMolePlacedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEMOVED.getPacketType())) {
      handleMoleMovedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.ASSIGNEDTOGAME.getPacketType())) {
      handleAssignedToGamePacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERJOINED.getPacketType())) {
      handlePlayerJoinedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSTURN.getPacketType())) {
      handlePlayersTurnPacket();
      timerRelatedController(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERPLACESMOLE.getPacketType())) {
      handlePlayerPlacesMolePacket();
      timerRelatedController(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEOVER.getPacketType())
      || packet.getPacketType().equalsIgnoreCase(Packets.GAMECANCELED.getPacketType())) {
      handleGameOverPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues() != null) {
        if (packet.getValues().get("message") != null) {
          if (ai.isDebug()) {
            System.out.println("Server sent: " + packet.getValues().get("message"));
          }
        }
      }
    } else {
      for (var packets : Packets.values()) {
        if (packets.getPacketType().equalsIgnoreCase(packet.getPacketType())) {
          if (ai.isDebug()) {
            System.out.println("The packet: " + packet.getPacketType() + " is not handled by the AI!");
            return;
          }
        }
      }
      if (ai.isDebug()) {
        System.out.println("Packet not found: " + packet.getPacketType());
      }
    }
  }

  private void timerRelatedController(@NotNull final AI ai) {
    try {
      if (ai.getSleepingTime() <= 0) {
        ai.setSleepingTime(0);
        ai.getLogic().handleAction(ai);
        return;
      }
      Thread.sleep((long) (ai.getSleepingTime() * 1000));
      ai.getLogic().handleAction(ai);
      if (isTimerRunning()) {
        timer.cancel();
        setTimerRunning(false);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
