package network.server;

import network.util.Network;
import network.util.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Network {

  private static final ArrayList<ServerThread> clientThreads = new ArrayList<>();
  private static final HashMap<Integer, ServerThread> threadIds = new HashMap<>();
  private static int threadId = 1;

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

  public static HashMap<Integer, ServerThread> getThreadIds() {
    return threadIds;
  }

  /**@author Carina
   * @use Due to a bug where we are getting the constructor which is not contructed at the time we create the Constructor and call the create object to create the sockets and stream
   * @see Server
   * @throws IOException
   */
  @SuppressWarnings("InfiniteLoopStatement")
  @Override
  public void create() throws IOException {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      System.out.println("Server listening on port " + getPort());
      while (true) {
        socket = serverSocket.accept();
        ServerThread serverThread = new ServerThread(socket, threadId);
        serverThread.start();
        getClientThreads().add(serverThread);
        threadIds.put(serverThread.getThreadId(), serverThread);
        threadId++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      socket.close();
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
  public synchronized void handlePacketRecieve(Packet packet, ServerThread reciever) {

    for (ServerThread threads : Server.getClientThreads()) {
      //Respone packet!
      if (threads.equals(reciever))
        threads.sendPacket(packet.appendMessage("vom server", true));
    }
  }
}
