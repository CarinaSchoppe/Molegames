package network.util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private final JSONObject jsonObject;
  private String packetType;



  public JSONObject getJsonObject() {
    return jsonObject;
  }

  /**
   * @param json is the JSONobject that will be send to the client
   * @author Carina
   * @use seperate the single objects in the string with a #
   */
  public Packet(JSONObject json){
   this.packetType = json.getString("type");
   this.jsonObject = json;
  }

  /**
   * @return the String that is the content of the packet
   * @author Carina
   * @use get the String from the packet and modify it
   */
  public void modifyType(String type) {
    packetType = type;
    jsonObject.put("type", packetType);
  }


  public String getPacketType() {
    return packetType;
  }


}
