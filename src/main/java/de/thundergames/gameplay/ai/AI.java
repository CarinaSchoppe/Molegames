/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "AI" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.gameplay.ai;

import de.thundergames.gameplay.ai.networking.AIClientThread;
import de.thundergames.gameplay.ai.networking.AIPacketHandler;
import de.thundergames.networking.client.Client;
import de.thundergames.play.util.Mole;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class AI extends Client implements Runnable {

  private final ArrayList<Mole> playerMolesInHoles = new ArrayList<>();
  private final ArrayList<Mole> playerMolesOnField = new ArrayList<>();
  private final int gameID;
  private boolean isMove = false;

  public AI(@NotNull final String ip, final int port, final int gameID) {
    super(port, ip, "AI");
    this.gameID = gameID;
    clientPacketHandler = new AIPacketHandler();
  }

  /**
   * @author Carina
   * @use is called to make a move!
   */
  private void makeMove() {
    System.out.println("AI makes a move");
  }

  /**
   * @author Carina
   * @use is called when an AI starts its job
   */
  @Override
  public void run() {
    boolean moveable = false;
    while (true) {
      if (isMove) {
        for (var mole : getPlayerMolesOnField()) {
          if (mole.isMoveable() && !moveable) {
            moveable = true;
            makeMove();
          }
        }
        if (!moveable) {
          for (var mole : getPlayerMolesInHoles()) {
            if (mole.isMoveable() && !moveable) {
              moveable = true;
              makeMove();
            }
          }
        }
        moveable = false;
        isMove = false;
      }
    }
  }

  public ArrayList<Mole> getPlayerMolesInHoles() {
    return playerMolesInHoles;
  }

  public ArrayList<Mole> getPlayerMolesOnField() {
    return playerMolesOnField;
  }

  public AIPacketHandler getAIPacketHandler() {
    return (AIPacketHandler) clientPacketHandler;
  }

  public AIClientThread getAIClientThread() {
    return (AIClientThread) clientThread;
  }

  public void setMove(boolean move) {
    isMove = move;
  }
}
