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
      System.out.println("AI: Im on turn!");
      ai.setDraw(true);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ai.getLogic().handlePlacement(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.DRAWNCARD.getPacketType())) {
      ai.setCardValue(true);
      ai.setCard(packet.getValues().getInt("card"));
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      ai.getLogic().moveMoles(ai);
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.JOINGAME.getPacketType())) {
      System.out.println("AI: Joined the game with id: " + ai.getGameID());
    } else if (packet.getPacketType().equalsIgnoreCase(Packets.MESSAGE.getPacketType())) {
      if (!packet.getValues().isEmpty() && !packet.getValues().toString().equalsIgnoreCase("{}")) {
        System.out.println("Server sended: " + packet.getValues().getString("message"));

      }
    } else if (packet.getPacketType().equals(Packets.MOLES.getPacketType())) {
      for (int i = 0; i < packet.getValues().getJSONArray("moles").toList().size(); i++) {
        ai.getMoleIDs().add(packet.getValues().getJSONArray("moles").getInt(i));
        System.out.println("MoleID is: " + ai.getMoleIDs().get(i));
      }
    } else if (packet.getPacketType().equals(Packets.OCCUPIED.getPacketType())) {
      if (!ai.isPlacedMoles()) {
        System.out.println("AI: placing mole again!");
        ai.getLogic().placeMoles(ai, packet.getValues().getInt("moleID"));
      }
    } else if (packet.getPacketType().equals(Packets.INGAME.getPacketType())) {
      System.out.println("Server sended: game with gameID: " + packet.getValues().getInt("gameID") + " is allready running!");
    } else if (packet.getPacketType().equals(Packets.LOGIN.getPacketType())) {
      ai.setClientID(packet.getValues().getInt("id"));
      ai.getClientThread().sendPacket(new Packet(new JSONObject().put("type", Packets.JOINGAME.getPacketType()).put("values", new JSONObject().put("gameID", ai.getGameID()).put("connectType", "player").put("ai", true).toString())));
    } else if (packet.getPacketType().equals(Packets.TURNOVER.getPacketType())) {
      System.out.println("AIs turn is over");
      ai.setDraw(false);
    } else if (packet.getPacketType().equals(Packets.PLACEMOLE.getPacketType())) {
      System.out.println("A mole was placed");
    } else if (packet.getPacketType().equals(Packets.NOTEXISTS.getPacketType())) {
      System.out.println("AI: The game you want to connect to does not exist!");
    } else if (packet.getPacketType().equals(Packets.INVALIDMOVE.getPacketType())) {
      System.out.println("AI has done in invalid move!");
    } else if (packet.getPacketType().equals(Packets.MOVEMOLE.getPacketType())) {
      System.out.println("AI: A mole has been moved!");
    } else if (packet.getPacketType().equals(Packets.MAP.getPacketType())) {
      System.out.println("AI: Recieved a new Map update!");
      ai.setMap(ai.getAiUtil().createMapFromJson(ai, packet.getValues()));
      ai.getMap().printMap();
    } else if (packet.getPacketType().equals(Packets.FULL.getPacketType())) {
      System.out.println("AI: the game I should join is allready full!");

    } else {
      throw new PacketNotExistsException(packet.getPacketType());
    }
  }

  public void randomPositionPacket(@NotNull final ClientThread clientThread, @NotNull final JSONObject object, @NotNull final JSONObject json) {
    System.out.println("AI: Does random move");
    var xZahl = new Random().nextInt(11);
    var yZahl = new Random().nextInt(11);
    json.put("x", xZahl);
    json.put("y", yZahl);
    object.put("values", json.toString());
    System.out.println("AI: moved / placed a mole at: " + xZahl + " " + yZahl);
    clientThread.sendPacket(new Packet(object));
  }
}
