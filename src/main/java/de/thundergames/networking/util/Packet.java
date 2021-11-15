/*
 * Copyright Notice                                             *
 * Copyright (c) ThunderGames 2021                              *
 * Created: 05.05.2018 / 11:59                                  *
 * All contents of this source text are protected by copyright. *
 * The copyright law, unless expressly indicated otherwise, is  *
 * at SwtPra10 | ThunderGames. All rights reserved              *
 * Any type of duplication, distribution, rental, sale, award,  *
 * Public accessibility or other use                            *
 * Requires the express written consent of ThunderGames.        *
 *
 */
package de.thundergames.networking.util;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * @author Carina
 * @use a Packet.class for the Packets that will be send between clients and the server
 */
public class Packet {

  private final JSONObject jsonObject;
  private final String packetType;

  /**
   * @param json is the JSONobject that will be send to the client
   * @author Carina
   * @use create a json object and add the type parameter
   */
  public Packet(@NotNull final JSONObject json) {
    this.packetType = json.getString("type");
    this.jsonObject = json;
  }

  public JSONObject getJsonObject() {
    return jsonObject;
  }

  public String getPacketType() {
    return packetType;
  }
}
