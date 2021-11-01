package network.util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private Object packetContent;
  private final JSONObject jsonObject = new JSONObject();
  private String packetType;
  private final ArrayList<String> messages = new ArrayList<>();

  /**
   * @param packetContent its the String and context of the packet that got send that will be send to a Server- or Clientthread
   * @param packetType    is the type of what kind of Packt we are handling
   * @author Carina
   * @use seperate the single objects in the string with a #
   */
  public Packet(String packetType, Object packetContent) {
    this.packetType = packetType;
    this.packetContent = packetContent;
    messages.addAll(List.of(packetContent.toString()));
    jsonObject.put("packetType", packetType);
    jsonObject.put("packetContent", packetContent);
  }

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  public void modifyType(String type) {
    packetType = type;
    jsonObject.put("packetType", packetType);
  }

  public Object getPacketContent() {
    return packetContent;
  }

  public ArrayList<String> getMessages() {
    return messages;
  }

  public Packet modifyMessage(String message) {
    this.packetContent = message;
    jsonObject.put("packetContent", packetContent);
    return this;
  }

  public String getPacketType() {
    return packetType;
  }

  public Packet appendMessage(String message, boolean prefix) {
    if (prefix) {
      packetContent = message + packetContent;
      jsonObject.put("packetContent", message + packetContent);
    } else {
      packetContent = packetContent + message;
      jsonObject.put("packetContent", packetContent + message);
    }
    return this;
  }
}
