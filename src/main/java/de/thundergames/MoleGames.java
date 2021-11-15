/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.11.21, 10:33 by Carina
 * Latest changes made by Carina on 15.11.21, 10:26
 * All contents of "MoleGames" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.ui.LoginScreen;
import de.thundergames.networking.client.Client;
import de.thundergames.networking.server.Server;
import de.thundergames.networking.util.PacketHandler;
import de.thundergames.play.game.GameLogic;
import de.thundergames.play.util.MultiGameHandler;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

/**
 * @author Carina
 * @use Main Class of the entire de.thundergames.game
 * @see Server as an instance that will be integrated
 * @see Client as an instance that will be integrated
 * @see AI as an instance that will be integrated
 */
public class MoleGames {

  private static MoleGames moleGames;
  private Server server;
  private MultiGameHandler gameHandler;
  private GameLogic gameLogic;
  private PacketHandler packetHandler;

  /**
   * @author Carina
   * @use MainClass start
   * @use creates a server object or AI object or client object depending on the arguments
   * @see Server
   * @see Client
   * @see AI
   */
  public static void main(@Nullable final String... args) {
    moleGames = new MoleGames();
    if (args.length == 0) {
      new LoginScreen().create(args);
    } else {
      switch (Objects.requireNonNull(args[0])) {
        case "-c":
        case "c":
          new de.thundergames.gameplay.gamemaster.ui.LoginScreen().create(args);
          break;
        case "-p":
        case "p":
          new LoginScreen().create(args);
          break;
        case "-s":
        case "s":
          moleGames.server = new Server(5000, "127.0.0.1");
          moleGames.packetHandler = new PacketHandler();
          moleGames.gameHandler = new MultiGameHandler();
          moleGames.gameLogic = new GameLogic();
          moleGames.server.create();
          break;
        case "-a":
        case "a":
          assert args[3] != null;
          new AI(
              Objects.requireNonNull(args[1]),
              Integer.parseInt(Objects.requireNonNull(args[2])),
              Integer.parseInt(Objects.requireNonNull(args[3])));
      }
    }
  }

  public static MoleGames getMoleGames() {
    return moleGames;
  }

  public MultiGameHandler getGameHandler() {
    return gameHandler;
  }

  public Server getServer() {
    return server;
  }

  public PacketHandler getPacketHandler() {
    return packetHandler;
  }

  public GameLogic getGameLogic() {
    return gameLogic;
  }
}
