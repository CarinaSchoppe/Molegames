/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 22.11.21, 14:50 by Carina latest changes made by Carina on 22.11.21, 10:56 All contents of "Packets" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
  GAMEPAUSED("gamePaused"),
  ENTERTOURNAMENT("enterTournament"),
  TOURNAMENTSTATERESPONSE("tournamentStateResponse"),
  TOURNAMENTPLAYERJOINED("tournamentPlayerJoined"),
  LEAVETOURNAMENT("leaveTournament"),
  TOURNAMENTPLAYERLEFT("tournamentPlayerLeft"),
  TOURNAMENTPLAYERKICKED("tournamentPlayerKicked"),
  TOURNAMENTPLAYERINGAME("tournamentPlayerInGame"),
  TOURNAMENTPLAYERINLOBBY("tournamentPlayerInLobby"),
  TOURNAMENTGAMESOVERVIEW("tournamentGamesOverview"),
  TOURNAMENTOVER("tournamentOver"),
  GETGAMEHISTORY("getGameHistory"),
  GAMEHISTORYRESPONE("gameHistoryResponse"),
  GETREMAININGTIME("getRemainingTime"),
  REMAININGTIME("remainingTime"),
  GAMESTARTED("gameStarted"),
  GAMEOVER("gameOver"),
  MESSAGE("message"),
  SCORENOTIFICATION("scoreNotification"),
  GETSCORE("getScore"),
  REGISTEROBSERVER("registerOverviewObserver"),
  UNREGISTEROBSERVER("unregisterOverviewObserver"),
  PLAYERKICKED("playerKicked"),
  PLAYERLEFT("playerLeft"),
  WELCOMEGAME("welcomeGame"),
  ASSIGNTOGAME("assignedToGame"),
  JOINGAME("joinGame"),
  LEAVEGAME("leaveGame"),
  LOGIN("login"),
  WELCOME("welcome"),
  LOGOUT("logout"),
  GAMECONTINUED("gameContinued"),
  GAMECANCELED("gameCanceled"),
  PLAYERPLACESMOLE("playerPlacedMole"),
  PLACEMOLE("placeMole"),
  MOLEPLACED("molePlaced"),
  PLAYERSTURN("playerTurn"),
  MAKEMOVE("makeMove"),
  MOLEMOVED("moleMoved"),
  MOVEPENALTYNOTIFICATION("movePenaltyNotification"),
  PLAYERSKIPPED("playerSkipped"),
  NEXTLEVEL("nextLevel"),
  GETOVERVIEW("getOverview"),
  OVERVIEW("overview"),
  PLAYERJOINED("playerJoined");

  final String packetType;

  Packets(String packetType) {
    this.packetType = packetType;
  }

  public String getPacketType() {
    return packetType;
  }
}
