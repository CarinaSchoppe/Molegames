package thundergames;

import game.util.MultiGameHandler;
import gameplay.ai.AI;
import network.client.Client;
import network.server.Server;

/**
 * @author Carina
 * @use Main Class of the entire game
 * @see Server as an instance that will be integrated
 * @see Client as an instance that will be integrated
 * @see AI as an instance that will be integrated
 */
public class MoleGames {

  private static MoleGames moleGames;
  private Server server;
  private MultiGameHandler gameHandler;
  private static final boolean keyListener = true;
  private AI AI;

  public static boolean isKeyListener() {
    return keyListener;
  }

  /**
   * @author Carina
   * @use MainClass start
   */
  public static void main(String... args) {
    moleGames = new MoleGames();
    if (args.length == 0) {
      Client.ClientMain();
    } else {
      switch (args[0]) {
        case "-p":
        case "p":
          Client.ClientMain();
          break;
        case "-s":
        case "s":
          moleGames.server = new Server(5000, "127.0.0.1");
          moleGames.gameHandler = new MultiGameHandler();
          moleGames.server.create();
          break;
        case "-a":
        case "a":
          new AI(true, args[1], Integer.parseInt(args[2]));
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
}
