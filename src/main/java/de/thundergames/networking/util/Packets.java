/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 13:02 by Carina latest changes made by Carina on 21.11.21, 13:02 All contents of "Packets" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */
package de.thundergames.networking.util;

/**
 * @author Carina
 * @see Packets the packet element that can be send
 */
public enum Packets {
  CREATEGAME("CREATE-GAME"),
  MESSAGE("MESSAGE"),
  DISCONNECT("DISCONNECT"),
  ERROR("ERROR"),
  FULL("FULL"),
  INGAME("INGAME"),
  NOTEXISTS("NOT-EXISTS"),
  CARDS("CARDS"),
  MOVEMOLE("MOVE-MOLE"),
  DRAWAGAIN("DRAW-AGAIN"),
  GAMEOVER("GAME-OVER"),
  WINS("WINS"),
  PLACEMOLE("PLACE-MOLE"),
  TURNTIME("TURNTIME"),
  LEAVEGAME("LEAVE-GAME"),
  MAP("MAP"),
  GAMEOVERVIEW("GET-GAME-OVERVIEW"),
  CREATEMAP("CREATE-MAP"),
  GAMESTART("GAME-START"),
  MOLEPLACED("MOLE-PLACED"),
  ALLMOLESPLACED("ALL-MOLES-PLACED"),
  PLAYERTURN("PLAYER-TURN"),
  PLAYERSUSPENDS("PLAYER-SUSPENDS"),
  DRAWCARD("DRAW-CARD"),
  DRAWNCARD("DRAWEN-CARD"),
  INVALIDMOVE("INVALID-MOVE"),
  KICKPLAYER("KICK-PLAYER"),
  NEXTFLOOR("NEXT-FLOOR"),
  NEXTPLAYER("NEXT-PLAYER"),
  CONFIGURATION("CONFIGURATION"),
  GAMEID("GAME-ID"),
  OCCUPIED("OCCUPIED"),
  MOLES("MOLES"),
  BACKTOLOBBY("BACK-TO-LOBBY"),
  GAMEPAUSE("GAME-PAUSED"),
  GAMERESUME("GAME-RESUME"),
  STOPGAME("STOP-GAME"),
  AI("AI"),
  GAMEEXISTS("GAME-EXISTS"),
  TURNOVER("TURN-OVER"),

  WELCOMEGAME("welcomeGame"),
  ASSIGNTOGAME("assignedToGame"),
  JOINGAME("joinGame"),
  LOGIN("login"),
  WELCOME("welcome"),
  LOGOUT("logout"),
  GETOVERVIEW("getOverview"),
  OVERVIEW("overview")
      ;
   final String packetType;

  Packets(String packetType) {
    this.packetType = packetType;
  }
  public String getPacketType() {
    return packetType;
  }
}
