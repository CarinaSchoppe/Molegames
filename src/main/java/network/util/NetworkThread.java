package network.util;

import network.client.Client;
import network.client.ClientThread;
import network.server.Server;
import network.server.ServerThread;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public abstract class NetworkThread extends Thread {
  protected final Socket socket;

  protected Packet packet;
  protected int id;
  private final PrintWriter writer;
  private final BufferedReader reader;

  /**
   * Creates a new NetworkThread.
   *
   * @param socket The socket to use.
   * @param id     the id of the serverSocketConnection
   */
  public NetworkThread(Socket socket, int id) throws IOException {
    this.socket = socket;
    this.id = id;
    if (this instanceof ServerThread)
      System.out.println("Connection established with id: " + id + "!");
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    writer = new PrintWriter(socket.getOutputStream(), true);
  }

  /**
   * @author Carina
   * creates a runnable that will create a listener for the incomming packets and reaches them over to
   * @use will be automaticlly started by a Server- or Client (Thread) it will wait for an incomming packetmessage than decrypts it and turns it into a Packet
   * @see readStringPacketInput method to use that packet for a client- or server handling
   */
  @Override
  public void run() {
    if (this instanceof ServerThread && !Server.isKeyboard()) {
      keyBoardListener(false);
      Server.setKeyboard(true);
    } else if (this instanceof ClientThread && Client.isKeyListener()) {
      keyBoardListener(true);
    }
    try {
      while (true) {
        if (socket.isConnected()) {
          String message = reader.readLine(); //ließt die packetmessage die reinkommt
          if (message != null) {
            JSONObject object = new JSONObject(message);
            if (!object.isNull("type")) {
              packet = new Packet(object);
              if ("DISCONNECT".equals(packet.getPacketType())) {
                System.out.println("Content: " + packet.getJsonObject().toString());
                disconnect();
                break;
              }
            }
            if (this.packet != null) {
              if (this instanceof ServerThread)
                System.out.println("Client with id: " + this.id + " sended: type: " + packet.getPacketType() + " contents: " + packet.getJsonObject().toString());
              readStringPacketInput(packet, this);
            }
          }
        } else {
          disconnect();
        }
      }
    } catch (SocketException e) {
      disconnect();
      System.out.println("Lost Socket Connection!");
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

  /**
   * @param client checks if the keyBoardlistener is started by a client or a server
   * @author Carina
   */
  private void keyBoardListener(boolean client) {
    new Thread(() -> {
      try {
        System.out.println("Keylistener started!");
        BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
          try {
            String message = keyboardReader.readLine();
            JSONObject object = new JSONObject();
            if (client) {
              object.put("type", Packets.MESSAGE.getPacketType());
              object.put("message", message);
              sendPacket(new Packet(object));
            } else {
              for (Iterator<ServerThread> iterator = Server.getClientThreads().iterator(); iterator.hasNext(); ) {
                ServerThread clientSocket = iterator.next();
                object.put("type", Packets.MESSAGE.getPacketType());
                object.put("message", message);
                clientSocket.sendPacket(new Packet(object));
              }
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();
  }

  /**
   * @param packet   that got read in by the runnable listener
   * @param receiver the one that it is recieving the thread of the server
   * @author Carina
   * @use it will automaticlly pass it forwards to the Server or Client to handle the Packet depending on who recieved it (Server- or Client thread)
   */
  public void readStringPacketInput(Packet packet, NetworkThread reciever) throws IOException {
    //TODO: How to handle the packet from the client! Player has moved -> now in a hole and than handle it
    if (reciever != null && packet != null) {
      if (reciever instanceof ServerThread) {
        PacketHandler.handlePacket(packet, (ServerThread) reciever);
      } else if (reciever instanceof ClientThread) {
        Client.getClient().getClientPacketHandler().handlePacket(Client.getClient(), packet);
      }
    }
  }

  /**
   * @param data is the packet that will be send in packet format but converted into a string seperated with #
   * @author Carina
   * @use create a Packet instance of a packet you want to send and pass it in in form of a string seperating the objects with #
   */
  public void sendPacket(Packet data) {
    writer.println(data.getJsonObject().toString());
  }

  /**
   * @author Carina
   * @use the basic logic of how a NetworkThread will disconnect
   */
  public abstract void disconnect();

  public int getConnectionId() {
    return id;
  }
}
