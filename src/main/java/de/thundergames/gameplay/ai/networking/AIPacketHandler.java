package de.thundergames.gameplay.ai.networking;

import de.thundergames.gameplay.ai.AI;
import de.thundergames.gameplay.player.networking.ClientPacketHandler;
import de.thundergames.gameplay.player.networking.ClientThread;
import de.thundergames.networking.util.Packet;
import de.thundergames.networking.util.PacketNotExistsException;
import de.thundergames.networking.util.Packets;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class AIPacketHandler extends ClientPacketHandler {

  public void handlePacket(@NotNull final AI ai, @NotNull final Packet packet) throws PacketNotExistsException {
    if (packet.getPacketType().equalsIgnoreCase(Packets.NEXTPLAYER.getPacketType())) {
      System.out.println("ich bin am zug!");
      ai.setDraw(true);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DRAWNCARD.getPacketType())) {
      ai.setCardValue(true);
      ai.setCard(packet.getValues().getInt("card"));
      System.out.println("AI Karte ist: " + packet.getValues().getInt("card"));
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      System.out.println("AI: Joined the game with id: " + ai.getGameID());
      ai.setAIThread(new Thread(ai));
      ai.getAIThread().start();
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      System.out.println("Server sended: " + packet.getValues().getString("message"));
    } else if (packet.getPacketType().equals(Packets.MOLES.getPacketType())) {
      for (int i = 0; i < packet.getValues().getJSONArray("moles").toList().size(); i++) {
        ai.getMoleIDs().add(packet.getValues().getJSONArray("moles").getInt(i));
        System.out.println("MoleID is: " + ai.getMoleIDs().get(i));
      }


    } else if (packet.getPacketType().equals(Packets.OCCUPIED.getPacketType())) {
      if (!ai.isPlacedMoles()) {
        System.out.println("AI: placing mole again!");
        ai.placeMoles(packet.getValues().getInt("moleID"));
      }
    } else if (packet.getPacketType().equals(Packets.INGAME.getPacketType())) {
      System.out.println("Server sended: game with gameID: " + packet.getValues().getInt("gameID") + " is allready running!");
    } else if (packet.getPacketType().equals(Packets.LOGIN.getPacketType())) {
      ai.setClientID(packet.getValues().getInt("id"));
      ai.getClientThread().sendPacket(new Packet(new JSONObject().put("type", Packets.JOINGAME.getPacketType()).put("values", new JSONObject().put("gameID", ai.getGameID()).put("connectType", "player").toString())));
    } else if (packet.getPacketType().equals(Packets.TURNOVER.getPacketType())) {
      System.out.println("AIs turn is over");
      ai.setDraw(false);
    } else if (packet.getPacketType().equals(Packets.PLACEMOLE.getPacketType())) {
      System.out.println("A mole was placed");
      ai.setDraw(false);
    } else {
      throw new PacketNotExistsException(packet.getPacketType());
    }
  }

  public void randomPositionPacket(@NotNull final ClientThread clientThread, @NotNull final JSONObject object, @NotNull final JSONObject json) {
    var xZahl = new Random().nextInt(11);
    var yZahl = new Random().nextInt(11);
    json.put("x", xZahl);
    json.put("y", yZahl);
    object.put("values", json.toString());
    clientThread.sendPacket(new Packet(object));
  }
}
