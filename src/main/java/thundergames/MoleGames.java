package blitzgames;

import game.util.MultiGameHandler;
import network.server.Server;

public class MoleGames {

  private static MoleGames moleGames;
  private Server server;
  private MultiGameHandler gameHandler;
  private static final boolean keyListener = true;

  public static boolean isKeyListener() {
    return keyListener;
  }

  /**
   * @author Carina
   * @use MainClass start
   */
  public static void main(String[] args) {
    moleGames = new MoleGames();
    moleGames.gameHandler = new MultiGameHandler();
    moleGames.server = new Server(5000, "127.0.0.1");
    moleGames.server.create();
    System.out.println("hallo!");
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
