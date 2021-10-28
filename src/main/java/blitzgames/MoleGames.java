package main;

import network.server.Server;

import java.io.IOException;

public class MoleGames {

  private static MoleGames moleGames;
  private Server server;

  /**
   * @author Carina
   * @use MainClass start
   */
  public static void main(String[] args) throws IOException {
    moleGames = new MoleGames();
    moleGames.server = new Server(5000, "127.0.0.1");
    moleGames.server.create();
  }

  public static MoleGames getMoleGames() {
    return moleGames;
  }

  public Server getServer() {
    return server;
  }
}
