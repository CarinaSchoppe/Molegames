package main;

import network.server.Server;
import network.util.CypherUtil;

public class MoleGames {

  private static MoleGames  moleGames;
  private  Server server;
  private CypherUtil cypherUtil;
  /**@author Carina
   * @use MainClass start */
  public static void main(String[] args) {
    moleGames = new MoleGames();
    moleGames.cypherUtil = new CypherUtil();
    moleGames.server = new Server(291220, "localhost");


  }

  public static MoleGames getMoleGames() {
    return moleGames;
  }

  public Server getServer() {
    return server;
  }

  public CypherUtil getCypherUtil() {
    return cypherUtil;
  }
}
