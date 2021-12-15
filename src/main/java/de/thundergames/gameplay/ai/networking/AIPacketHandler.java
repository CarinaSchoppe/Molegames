/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:23 by Carina Latest changes made by Carina on 15.12.21, 19:22 All contents of "AIPacketHandler" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import de.thundergames.playmechanics.map.Map;
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
   * @param packet the packet recieved
   * @author Carina
   * @use the logic for the AI to decide what to do depending on the packet recieved
   */
  public synchronized void handlePacket(@NotNull final AI ai, @NotNull final Packet packet) {
    this.packet = packet;
    if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOME.getPacketType())) {
      if (!packet.getValues().get("magic").getAsString().equals("mole42")) {
        System.exit(3);
      }
      ai.getClientThread().setThreadID(packet.getValues().get("clientID").getAsInt());
      ai.setPlayer(new Player(ai));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.WELCOMEGAME.getPacketType())) {
      handleWelcomeGamePacket();
      ai.setMap(new Map(ai.getGameState().getFloor().getHoles(), ai.getGameState().getFloor().getDrawAgainFields(), ai.getGameState().getFloor().getPoints()));
      ai.getMap().build(ai.getGameState());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTLEVEL.getPacketType())) {
      handleNextFloorPacket();
      ai.setMap(new Map(ai.getGameState().getFloor().getHoles(), ai.getGameState().getFloor().getDrawAgainFields(), ai.getGameState().getFloor().getPoints()));
      ai.getMap().build(ai.getGameState());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEPLACED.getPacketType())) {
      handleMolePlacedPacket();
      ai.setMap(new Map(ai.getGameState().getFloor().getHoles(), ai.getGameState().getFloor().getDrawAgainFields(), ai.getGameState().getFloor().getPoints()));
      ai.getMap().build(ai.getGameState());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MOLEMOVED.getPacketType())) {
      handleMoleMovedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERJOINED.getPacketType())) {
      handlePlayerJoinedPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERSTURN.getPacketType())) {
      handlePlayersTurnPacket();
      timerRelatedController(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.PLAYERPLACESMOLE.getPacketType())) {
      handlePlayerPlacesMolePacket();
      timerRelatedController(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.GAMEOVER.getPacketType()) || packet.getPacketType().equalsIgnoreCase(Packets.GAMECANCELED.getPacketType())) {
      handleGameOverPacket();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (packet.getValues() != null) {
        if (packet.getValues().get("message") != null) {
          if (ai.isDebug()) System.out.println("Server sended: " + packet.getValues().get("message").getAsString());
        }
      }
    }
  }

  private synchronized void timerRelatedController(@NotNull final AI ai) {
    try {
      if (ai.getSleepingTime() < 0) {
        ai.setSleepingTime(0);
      }
      Thread.sleep(ai.getSleepingTime());
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
