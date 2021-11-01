package network.server;

import game.util.Game;
import network.util.Network;
import network.util.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Server extends Network {

  private static final ArrayList<ServerThread> clientThreads = new ArrayList<>();
  private static final HashMap<Integer, ServerThread> threadIds = new HashMap<>();
  private static int threadId = 0;
  private static boolean keyboard = false;

  public static void setKeyboard(boolean keyboard) {
    Server.keyboard = keyboard;
  }

  public static boolean isKeyboard() {
    return keyboard;
  }

  /**
   * @param port obvious the Serverport in case of empty localhost
   * @param ip   obvious the ServerIp in case of empty localhost
   * @author Carina
   * @use creates a Server with a @param serverSocket and uses this one to create a ServerThread which will handle the Inputreading and got info about the Outputsending
   * adds every ServerThread to a List and adds an Id to it and puts that into a Map
   */
  public Server(int port, String ip) {
    super(port, ip);
  }

  @Override
  public void create() {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Server listening on port " + getPort());
      while (true) {
        socket = serverSocket.accept();
        ServerThread serverThread = new ServerThread(socket, threadId);
        serverThread.start();
        serverThread.sendPacket(new Packet("ID#" + threadId));
        getClientThreads().add(serverThread);
        threadIds.put(serverThread.getConnectionId(), serverThread);
        threadId++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static HashMap<Integer, ServerThread> getThreadIds() {
    return threadIds;
  }
  /**
   * @throws IOException
   * @author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we create the Constructor and call the create object to create the sockets and stream
   * @see Server
   */

  /**
   * @param clients Arraylist of the clients that are connected
   * @param packet  the packet that should be send
   * @use the method will send a packet to all connected clients of the game
   */
  //WICHTIG: BEDENKE mach dies immer in einem anderen Thread oder der Mainthread muss sicher frei sein!
  public static synchronized void sendToGameClients(Game game, Packet packet) {
    try {
      if (!game.getClients().isEmpty()) {
        for (Iterator<ServerThread> iterator = game.getClients().iterator(); iterator.hasNext(); ) {
          ServerThread client = iterator.next();
          client.sendPacket(packet);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<ServerThread> getClientThreads() {
    return clientThreads;
  }
  /**
   * @param packet   the packet that the server recieved
   * @param reciever which Serverthread has send this packet
   * @author Carina
   * @use sends handles the Packet and sends it to all other players and than sends a responePacket to the Users
   */
}
