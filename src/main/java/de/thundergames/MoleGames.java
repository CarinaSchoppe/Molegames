package de.thundergames;

import de.thundergames.game.util.GameLogic;
import de.thundergames.game.util.MultiGameHandler;
import de.thundergames.gameplay.ai.AI;
import de.thundergames.network.client.Client;
import de.thundergames.network.server.Server;
import de.thundergames.network.util.PacketHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
  private AI AI;
  private GameLogic gameLogic;
  private PacketHandler packetHandler;

  /**
   * @author Carina
   * @use MainClass start
   */
  public static void main(@Nullable final String... args) {
    moleGames = new MoleGames();
    if (args.length == 0) {
      Client.ClientMain();
    } else {
      switch (Objects.requireNonNull(args[0])) {
        case "-c":
        case "c":
        case "-p":
        case "p":
          Client.ClientMain();
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
          assert args[2] != null;
          moleGames.AI = new AI(true, args[1], Integer.parseInt(args[2]));
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
}
