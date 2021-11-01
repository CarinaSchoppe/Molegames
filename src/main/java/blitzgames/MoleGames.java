package blitzgames;

import network.server.Server;

import java.io.IOException;

public class MoleGames {

  private static MoleGames moleGames;
  private Server server;
  private static boolean keyListener = true;

  public static boolean isKeyListener() {
    return keyListener;
  }

  /**
   * @author Carina
   * @use MainClass start
   */
  public static void main(String[] args) throws IOException {
    moleGames = new MoleGames();
    moleGames.server = new Server(5000, "127.0.0.1");
    moleGames.server.create();
    System.out.println("hallo!");
  }

  public static MoleGames getMoleGames() {
    return moleGames;
  }

  public Server getServer() {
    return server;
  }
}
