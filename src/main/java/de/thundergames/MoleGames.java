/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:18 by Carina Latest changes made by Carina on 24.12.21, 12:16
 * All contents of "MoleGames" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.ausrichter.AusrichterClient;
import de.thundergames.gameplay.ausrichter.ui.MainGUI;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.LoginScreen;
import de.thundergames.networking.server.Server;
import de.thundergames.playmechanics.util.MultiGameHandler;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Carina
 * @use Main Class of the entire de.thundergames.game
 * @see Server as an instance that will be integrated
 * @see Client as an instance that will be integrated
 * @see AI as an instance that will be integrated
 */
@Getter
@Setter
public class MoleGames {

  private static MoleGames MOLE_GAMES;
  private AI ai;
  private Server server;
  private MultiGameHandler gameHandler;
  private AusrichterClient ausrichterClient;
  private MainGUI gui;

  /**
   * @author Carina
   * @use Mainclass start method
   * @use creates a server object or AI object or client object depending on the arguments
   * @see Server
   * @see Client
   * @see AI
   */
  public static void main(@Nullable final String... args) {
    MOLE_GAMES = new MoleGames();
    if (args.length == 0) {
      LoginScreen.create(args);
    } else {
      switch (Objects.requireNonNull(args[0])) {
        case "-p":
        case "p":
          LoginScreen.create(args);
          break;
        case "-s":
        case "s":
          MOLE_GAMES.server = new Server(5000, "127.0.0.1");
          MOLE_GAMES.gameHandler = new MultiGameHandler();
          MOLE_GAMES.server.create();
          MOLE_GAMES.gui = new MainGUI();
          MainGUI.create(MOLE_GAMES.server);
          break;
        case "-a":
        case "a":
          if (args.length == 3) {
            MoleGames.getMoleGames().ai =
                new AI(
                    Objects.requireNonNull(args[1]),
                    Integer.parseInt(Objects.requireNonNull(args[2])),
                    Integer.parseInt(Objects.requireNonNull(args[3])));
          } else if (args.length == 2) {
            MoleGames.getMoleGames().ai =
                new AI(
                    Objects.requireNonNull(args[1]),
                    Integer.parseInt(Objects.requireNonNull(args[2])));
          }
          MoleGames.getMoleGames().ai.create();
      }
    }
  }

  public static MoleGames getMoleGames() {
    return MOLE_GAMES;
  }
}
