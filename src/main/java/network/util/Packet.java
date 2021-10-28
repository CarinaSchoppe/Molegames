package network.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private String packetContent;
  private String packetType;
  private final ArrayList<String> messages = new ArrayList<>();

  /**
   * @param packetContent its the String and context of the packet that got send that will be send to a Server- or Clientthread
   * @param packetType    is the type of what kind of Packt we are handling
   * @author Carina
   * @use seperate the single objects in the string with a #
   */
  public Packet(String packetType, String packetContent) {
    this.packetType = packetType;
    this.packetContent = packetContent != null ? packetContent : "DISCONNECT";
    messages.addAll(List.of(this.packetContent.split("#")));
  }

  public Packet(String packetContent) {
    this.packetType = "DEFAULT";
    this.packetContent = packetContent != null ? packetContent : "DISCONNECT";
    messages.addAll(List.of(this.packetContent.split("#")));
  }

  public String getPacketContent() {
    return packetContent;
  }

  protected Packet() {
  }

  /**
   * @param data the packet that was send through the network
   * @return a new Packet that will be encrypted by Base64
   * @author Carina
   */
  //TODO: Implementation
  public static Packet encrypt(Packet data) {
    return new Packet(new String(Base64.getEncoder().encode(data.getPacketContent().getBytes())));
  }

  public static Packet decrypt(Packet data) {
    return new Packet(new String(Base64.getDecoder().decode(data.getPacketContent().getBytes())));
  }

  public ArrayList<String> getMessages() {
    return messages;
  }

  public static String decrypt(String data) {
    return new String(Base64.getDecoder().decode(data.getBytes()));
  }

  public synchronized Packet modifyMessage(String message) {
    this.packetContent = message;
    return this;
  }

  public synchronized Packet appendMessage(String message, boolean prefix) {
    if (prefix) packetContent = message + packetContent;
    else packetContent = packetContent + message;
    return this;
  }
}