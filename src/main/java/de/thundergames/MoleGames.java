/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 15:19 by Carina latest changes made by Carina on 21.11.21, 15:11 All contents of "MoleGames" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.ausrichter.GameMasterClient;
import de.thundergames.gameplay.player.networking.Client;
import de.thundergames.gameplay.player.ui.LoginScreen;
import de.thundergames.networking.server.PacketHandler;
import de.thundergames.networking.server.Server;
import de.thundergames.playmechanics.game.GameLogic;
import de.thundergames.playmechanics.util.MultiGameHandler;
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
  private AI ai;
  private Server server;
  private MultiGameHandler gameHandler;
  private GameLogic gameLogic;
  private PacketHandler packetHandler;
  private GameMasterClient gameMasterClient;

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
          new de.thundergames.gameplay.ausrichter.ui.LoginScreen().create(args);
          break;
        case "-a":
        case "a":
          assert args[3] != null;
          MoleGames.getMoleGames().ai =
              new AI(
                  Objects.requireNonNull(args[1]),
                  Integer.parseInt(Objects.requireNonNull(args[2])),
                  Integer.parseInt(Objects.requireNonNull(args[3])));
          MoleGames.getMoleGames().ai.create();
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

  public GameMasterClient getGameMasterClient() {
    return gameMasterClient;
  }

  public void setGameMasterClient(GameMasterClient gameMasterClient) {
    this.gameMasterClient = gameMasterClient;
  }


}
