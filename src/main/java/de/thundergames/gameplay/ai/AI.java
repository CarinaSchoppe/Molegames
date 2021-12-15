/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:20 All contents of "AI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.ai;

import de.thundergames.gameplay.ai.networking.AIClientThread;
import de.thundergames.gameplay.ai.networking.AIPacketHandler;
import de.thundergames.gameplay.player.Client;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class AI extends Client {

  private final int gameID;
  private AILogic logic;
  private int card;
  private boolean placedMoles = false;
  private int placedMolesAmount = 0;
  private final int SLEEPING_TIME = 0;

  /**
   * @param ip
   * @param port
   * @param gameID
   * @author Carina
   * @use creates the AI instance needed to run the AI
   */
  public AI(@NotNull final String ip, final int port, final int gameID) {
    super(port, ip, "AI");
    this.gameID = gameID;
  }

  @Override
  public void create() {
    CLIENT = this;
    clientPacketHandler = new AIPacketHandler(this);
    logic = new AILogic(this);
    connect();
  }

  /**
   * @author Carina
   * @use connects the AI to the server
   */
  @Override
  public void connect() {
    try {
      socket = new Socket(ip, port);
      clientThread = new AIClientThread(socket, 0, this);
      clientThread.start();
      clientPacketHandler.loginPacket(getName());
      clientPacketHandler.joinGamePacket(gameID, true);
    } catch (IOException exception) {
      if (isDebug())
        System.out.println("Is the server running?!");
    }
  }

  public AIPacketHandler getAIPacketHandler() {
    return (AIPacketHandler) clientPacketHandler;
  }
}
