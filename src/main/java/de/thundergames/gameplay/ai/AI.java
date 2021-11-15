/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra 10 2021
 * File created on 15.11.21, 10:24 by Carina
 * Latest changes made by Carina on 15.11.21, 10:22
 * All contents of "AI" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra 10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra 10.
 */
package de.thundergames.gameplay.ai;

import de.thundergames.play.util.Mole;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

public class AI implements Runnable {

  private final ArrayList<Mole> playerMolesInHoles = new ArrayList<>();
  private final ArrayList<Mole> playerMolesOnField = new ArrayList<>();
  private final Thread AIthread = new Thread(this);
  private final int port;
  private final String ip;
  private final int gameID;
  private boolean isMove = false;

  public AI(@NotNull final String ip, final int port, final int gameID) {
    this.ip = ip;
    this.port = port;
    this.gameID = gameID;
  }

  /**
   * @author Carina
   * @use is called after the constructor to initiate everything needed than starts the AI
   */
  public void start() {
    AIthread.start();
    isMove = true;
  }

  private void makeMove() {}

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
}
