/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 12:04 by Carina Latest changes made by Carina on 09.01.22, 12:04 All contents of "Packets" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */ket element that can be sent
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
  TOURNAMENTSCORE("tournamentScore"),
  GETTOURNAMENTSCORE("getTournamentScore"),
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

  Packets(@NotNull final String packetType) {
    this.packetType = packetType;
  }

  public String getPacketType() {
    return packetType;
  }
}
