package network.util;

import main.MoleGames;
import network.client.Client;
import network.client.ClientThread;
import network.server.ServerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class NetworkThread extends Thread {
  protected Socket socket;
  protected Packet packet;
  private PrintWriter writer;
  private BufferedReader reader;
  public NetworkThread(Socket socket) {
    this.socket = socket;
  }

  /**
   * @author Carina
   * creates a runnable that will create a listener for the incomming packets and reaches them over to
   * @use will be automaticlly started by a Server- or Client (Thread) it will wait for an incomming packetmessage than decrypts it and turns it into a Packet
   * @see readStringPacketInput method to use that packet for a client- or server handling
   */
  @Override
  public void run() {
    try {
      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      writer = new PrintWriter(socket.getOutputStream(), true);
      while (true) {
        if (socket.isConnected()) {
          packet = new Packet(MoleGames.getMoleGames().getCypherUtil().decrypt(reader.readLine()));
          if ("DISCONNECT".equals(packet.getPacketString()) || packet.getPacketString().equals(null)) {
            disconnect();
            break;
          }
          if (!"".equals(packet.getPacketString()))
            readStringPacketInput(packet, this);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param packet   that got read in by the runnable listener
   * @param reciever the one that it is recieving the thread of the server
   * @author Carina
   * @use it will automaticlly pass it forwards to the Server or Client to handle the Packet depending on who recieved it (Server- or Client thread)
   */
  public synchronized void readStringPacketInput(Packet packet, NetworkThread reciever) {
    //TODO: How to handle the packet from the client! Player has moved -> now in a hole and than handle it
    if (reciever instanceof ServerThread) {
      MoleGames.getMoleGames().getServer().handlePacketRecieve(packet, (ServerThread) reciever);
    } else if (reciever instanceof ClientThread) {
      Client.handlePacket(packet);
    }
  }

  /**
   * @param data is the packet that will be send in packet format but converted into a string seperated with #
   * @author Carina
   * @use create a Packet instance of a packet you want to send and pass it in in form of a string seperating the objects with #
   */
  public synchronized void sendPacket(Packet data) {
    writer.println(MoleGames.getMoleGames().getCypherUtil().encrypt(data).getPacketString());
  }

  /**
   * @author Carina
   * @usage the basic logic of how a NetworkThread will disconnect
   */
  public abstract void disconnect();
}
