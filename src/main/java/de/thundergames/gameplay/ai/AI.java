/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 18:17 by Carina latest changes made by Carina on 02.12.21, 18:17
 * All contents of "AI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.ai;

import de.thundergames.gameplay.ai.networking.AIClientThread;
import de.thundergames.gameplay.ai.networking.AIPacketHandler;
import de.thundergames.gameplay.player.networking.Client;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class AI extends Client {

  private final int gameID;
  private final AILogic logic;
  private final AIUtil aiUtil;
  private int card;
  private boolean placedMoles = false;
  private int placedMolesAmount = 0;

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
    clientPacketHandler = new AIPacketHandler();
    logic = new AILogic();
    aiUtil = new AIUtil();
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
      clientPacketHandler.loginPacket(this, getName());
      clientPacketHandler.joinGamePacket(this, gameID, true);
    } catch (IOException exception) {
      System.out.println("Is the server running?!");
    }
  }

  public AIPacketHandler getAIPacketHandler() {
    return (AIPacketHandler) clientPacketHandler;
  }

  public AIUtil getAIUtil() {
    return aiUtil;
  }

  public int getCard() {
    return card;
  }

  public void setCard(int card) {
    this.card = card;
  }

  public boolean isPlacedMoles() {
    return placedMoles;
  }

  public void setPlacedMoles(boolean placedMoles) {
    this.placedMoles = placedMoles;
  }

  public AILogic getLogic() {
    return logic;
  }


  public int getPlacedMolesAmount() {
    return placedMolesAmount;
  }

  public void setPlacedMolesAmount(int placedMolesAmount) {
    this.placedMolesAmount = placedMolesAmount;
  }


}
