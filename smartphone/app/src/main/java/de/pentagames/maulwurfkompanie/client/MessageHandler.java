package de.pentagames.maulwurfkompanie.client;

import android.content.Intent;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.pentagames.maulwurfkompanie.Utils;
import de.pentagames.maulwurfkompanie.board.Board;
import de.pentagames.maulwurfkompanie.board.GameView;
import de.pentagames.maulwurfkompanie.board.ShownDisc;
import de.pentagames.maulwurfkompanie.board.ShownMole;
import de.pentagames.maulwurfkompanie.ui.GameActivity;
import de.pentagames.maulwurfkompanie.ui.LoginActivity;
import de.pentagames.maulwurfkompanie.ui.lobby.LobbyActivity;
import upb.maulwurfcompany.library.Message;
import upb.maulwurfcompany.library.data.Game;
import upb.maulwurfcompany.library.data.GameState;
import upb.maulwurfcompany.library.data.GameStatus;
import upb.maulwurfcompany.library.data.MovePenalty;
import upb.maulwurfcompany.library.data.TournamentState;
import upb.maulwurfcompany.library.messages.AssignedToGame;
import upb.maulwurfcompany.library.messages.BadArgumentsError;
import upb.maulwurfcompany.library.messages.BadMessageError;
import upb.maulwurfcompany.library.messages.GameCanceled;
import upb.maulwurfcompany.library.messages.GameContinued;
import upb.maulwurfcompany.library.messages.GameHistoryResponse;
import upb.maulwurfcompany.library.messages.GameOver;
import upb.maulwurfcompany.library.messages.GamePaused;
import upb.maulwurfcompany.library.messages.GameStarted;
import upb.maulwurfcompany.library.messages.InvalidMoveError;
import upb.maulwurfcompany.library.messages.JoinGame;
import upb.maulwurfcompany.library.messages.LeaveGame;
import upb.maulwurfcompany.library.messages.Login;
import upb.maulwurfcompany.library.messages.MakeMove;
import upb.maulwurfcompany.library.messages.MoleMoved;
import upb.maulwurfcompany.library.messages.MolePlaced;
import upb.maulwurfcompany.library.messages.MovePenaltyNotification;
import upb.maulwurfcompany.library.messages.NextLevel;
import upb.maulwurfcompany.library.messages.NotAllowedError;
import upb.maulwurfcompany.library.messages.NotFoundError;
import upb.maulwurfcompany.library.messages.Overview;
import upb.maulwurfcompany.library.messages.PlaceMole;
import upb.maulwurfcompany.library.messages.PlayerJoined;
import upb.maulwurfcompany.library.messages.PlayerKicked;
import upb.maulwurfcompany.library.messages.PlayerLeft;
import upb.maulwurfcompany.library.messages.PlayerPlacesMole;
import upb.maulwurfcompany.library.messages.PlayerSkipped;
import upb.maulwurfcompany.library.messages.PlayersTurn;
import upb.maulwurfcompany.library.messages.RegisterOverviewObserver;
import upb.maulwurfcompany.library.messages.RemainingTime;
import upb.maulwurfcompany.library.messages.ScoreNotification;
import upb.maulwurfcompany.library.messages.TournamentGamesOverview;
import upb.maulwurfcompany.library.messages.TournamentOver;
import upb.maulwurfcompany.library.messages.TournamentPlayerInGame;
import upb.maulwurfcompany.library.messages.TournamentPlayerInLobby;
import upb.maulwurfcompany.library.messages.TournamentPlayerJoined;
import upb.maulwurfcompany.library.messages.TournamentPlayerKicked;
import upb.maulwurfcompany.library.messages.TournamentPlayerLeft;
import upb.maulwurfcompany.library.messages.TournamentScore;
import upb.maulwurfcompany.library.messages.TournamentStateResponse;
import upb.maulwurfcompany.library.messages.UndefinedError;
import upb.maulwurfcompany.library.messages.UnregisterOverviewObserver;
import upb.maulwurfcompany.library.messages.Welcome;
import upb.maulwurfcompany.library.messages.WelcomeGame;

public class MessageHandler {

  private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();
  private final Thread thread;

  public Overview overview;
  public Game game;
  public GameState currentGameState;
  public TournamentState tournamentState;
  public long until = 0;

  public MessageHandler() {
    this.thread = new Thread(() -> {
      try {
        while (true)
          handleMessage(messages.take());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    this.thread.start();
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
  }

  public void close() {
    if (this.thread.isAlive()) this.thread.interrupt();
  }

  public void offerMessage(Message message) {
    this.messages.offer(message);
  }

  private void handleMessage(Message message) {
    try {
      System.out.println("got " + Message.getMessageType(message.getClass()));
      if (message instanceof AssignedToGame) assignedToGame((AssignedToGame) message);
      else if (message instanceof BadArgumentsError)
        badArgumentsError((BadArgumentsError) message);
      else if (message instanceof BadMessageError) badMessageError((BadMessageError) message);
      else if (message instanceof GameCanceled) gameCanceled((GameCanceled) message);
      else if (message instanceof GameContinued) gameContinued((GameContinued) message);
      else if (message instanceof GameHistoryResponse)
        gameHistoryResponse((GameHistoryResponse) message);
      else if (message instanceof GameOver) gameOver((GameOver) message);
      else if (message instanceof GamePaused) gamePaused((GamePaused) message);
      else if (message instanceof GameStarted) gameStarted((GameStarted) message);
      else if (message instanceof InvalidMoveError)
        invalidMoveError((InvalidMoveError) message);
      else if (message instanceof MoleMoved) moleMoved((MoleMoved) message);
      else if (message instanceof MolePlaced) molePlaced((MolePlaced) message);
      else if (message instanceof MovePenaltyNotification)
        movePenaltyNotification((MovePenaltyNotification) message);
      else if (message instanceof NextLevel) nextLevel((NextLevel) message);
      else if (message instanceof NotAllowedError) notAllowedError((NotAllowedError) message);
      else if (message instanceof NotFoundError) notFoundError((NotFoundError) message);
      else if (message instanceof Overview) overview((Overview) message);
      else if (message instanceof PlayerJoined) playerJoined((PlayerJoined) message);
      else if (message instanceof PlayerKicked) playerKicked((PlayerKicked) message);
      else if (message instanceof PlayerLeft) playerLeft((PlayerLeft) message);
      else if (message instanceof PlayerPlacesMole)
        playerPlacesMole((PlayerPlacesMole) message);
      else if (message instanceof PlayerSkipped) playerSkipped((PlayerSkipped) message);
      else if (message instanceof PlayersTurn) playersTurn((PlayersTurn) message);
      else if (message instanceof RemainingTime) remainingTime((RemainingTime) message);
      else if (message instanceof ScoreNotification)
        scoreNotification((ScoreNotification) message);
      else if (message instanceof TournamentGamesOverview)
        tournamentGamesOverview((TournamentGamesOverview) message);
      else if (message instanceof TournamentOver) tournamentOver((TournamentOver) message);
      else if (message instanceof TournamentPlayerInGame)
        tournamentPlayerInGame((TournamentPlayerInGame) message);
      else if (message instanceof TournamentPlayerInLobby)
        tournamentPlayerInLobby((TournamentPlayerInLobby) message);
      else if (message instanceof TournamentPlayerJoined)
        tournamentPlayerJoined((TournamentPlayerJoined) message);
      else if (message instanceof TournamentPlayerKicked)
        tournamentPlayerKicked((TournamentPlayerKicked) message);
      else if (message instanceof TournamentPlayerLeft)
        tournamentPlayerLeft((TournamentPlayerLeft) message);
      else if (message instanceof TournamentScore) tournamentScore((TournamentScore) message);
      else if (message instanceof TournamentStateResponse)
        tournamentStateResponse((TournamentStateResponse) message);
      else if (message instanceof UndefinedError) undefinedError((UndefinedError) message);
      else if (message instanceof WelcomeGame) welcomeGame((WelcomeGame) message);
      else if (message instanceof Welcome) welcome((Welcome) message);
      // TODO else error
    } catch (Exception e) {
      System.out.println("error during message processing");
      e.printStackTrace();
    }
  }

  /**
   * The message is sent in the "down" Direction.
   * Server notifies the client that it has been assigned to a certain game by sending the game’s ID.
   */
  private void assignedToGame(@NotNull final AssignedToGame message) {
    this.game = null;
    for (var game : this.overview.games) {
      if (game.gameID == message.gameID) {
        Utils.makeToast("Sie wurden einem Spiel hinzugefügt " + game.gameID, true);
        joinGame(game, true); // TODO join as participant?
      }
    }
    if (this.game == null)
      Utils.makeToast("Zugewiesenes Spiel wurde nicht gefunden", true);
  }

  /**
   * This method handles messages of the type BadArgumentsError (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void badArgumentsError(@NotNull final BadArgumentsError message) {
    Utils.makeToast(message.description, true);
  }

  /**
   * This method handles messages of the type BadMessageError (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void badMessageError(@NotNull final BadMessageError message) {
    Utils.makeToast(message.description, true);
  }

  /**
   * This method handles messages of the type GameCanceled (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void gameCanceled(@NotNull final GameCanceled message) {
    until = 0;
    Utils.makeToast("Dieses Spiel wurde leider abgebrochen...", true);
    // TODO return to overview/Lobby
  }

  /**
   * This method handles messages of the type GameContinued (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void gameContinued(@NotNull final GameContinued message) {
  }

  /**
   * This method handles messages of the type GameHistoryResponse (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void gameHistoryResponse(@NotNull final GameHistoryResponse message) {
    // TODO should we replace the currentGameState with initialGameState + all handled old Messages?
    for (var oldMessage : message.history) {
      if (oldMessage.getClass() != message.getClass()) {
        // old GameHistoryResponses shouldn't call this method again
        handleMessage(oldMessage);
      }
    }
  }

  /**
   * This method handles messages of the type GameOver (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void gameOver(@NotNull final GameOver message) {
    until = 0;
    currentGameState.status = GameStatus.OVER;
    game.result = message.result;
    currentGameState.score = message.result;
    Utils.makeToast("Das Spiel ist zuende!", true);
    // TODO return to overview/Lobby
  }

  /**
   * This method handles messages of the type GamePaused (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void gamePaused(@NotNull final GamePaused message) {
    Utils.makeToast("Das Spiel wurde unterbrochen.", true);
  }

  /**
   * This method handles messages of the type GameStarted (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void gameStarted(@NotNull final GameStarted message) {
    currentGameState = message.initialGameState;
    Utils.makeToast("Das Spiel startet!", true);
  }

  /**
   * This method handles messages of the type InvalidMoveError (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void invalidMoveError(@NotNull final InvalidMoveError message) {
    Utils.makeToast("UPS, dieser Zug war nicht ganz richtig!", true);
  }

  /**
   * This method handles messages of the type MoleMoved (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void moleMoved(@NotNull final MoleMoved message) {
    until = 0;
    var mole = Board.getShownMole(currentGameState, message.from);
    Objects.requireNonNull(mole).animateTo(message.to);
    // remove the pull disc which was used
    var pullDiscs = Utils.arrayRemove(Objects.requireNonNull(currentGameState.pullDiscs.get(mole.mole.player.clientID)), message.pullDisc);
    if (pullDiscs.length == 0) pullDiscs = Utils.intToIntegerArray(game.pullDiscs);
    currentGameState.pullDiscs.put(mole.mole.player.clientID, pullDiscs);
    ShownDisc.moveSuccessful(message);
  }

  /**
   * This method handles messages of the type MolePlaced (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void molePlaced(@NotNull final MolePlaced message) {
    until = 0;
    currentGameState.placedMoles = Utils.arrayPush(currentGameState.placedMoles, message.mole);
    GameView.instance.shownMoles.add(new ShownMole(message.mole));
  }

  /**
   * This method handles messages of the type MovePenaltyNotification (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void movePenaltyNotification(@NotNull final MovePenaltyNotification message) {
    until = 0;
    int playerPositionInArray = -10000;
    for (var i = 0; i < currentGameState.activePlayers.length; i++) {
      if (currentGameState.activePlayers[i].clientID == message.player.clientID) {
        playerPositionInArray = i;
      }
    }
    if (message.penalty.equals(MovePenalty.NOTHING)) {
      // when Penalty is set to nothing
      if (Utils.isThatUs(message.player)) {
        // if this affects us:
        Utils.makeToast("Gerade nochmal Glück gehabt, keine Strafe!", true);
      } else {
        Utils.makeToast("Spieler " + message.player.name + " wurde nicht bestraft.", true);
      }
    } else if (message.penalty.equals(MovePenalty.POINT_DEDUCTION)) {
      // when Penalty is set to point deduction
      var newPoints = currentGameState.score.points.get(playerPositionInArray) - game.deductedPoints;
      currentGameState.score.points.remove(playerPositionInArray);
      currentGameState.score.points.put(playerPositionInArray, newPoints);
      if (Utils.isThatUs(message.player)) {
        // if this affects us:
        Utils.makeToast("Dir wurden " + game.deductedPoints + " Punkte für deinen Zug abgezogen.", true);
      } else {
        Utils.makeToast("Spieler " + message.player.name + " wurde nicht bestraft.", true);
      }
    } else if (message.penalty.equals(MovePenalty.KICK)) {
      // when Penalty is set to kick
      // nothing to  see here, Server should send a PlayerKicked Message which creates Toasts
      // and removes the player from currentgameState.activePlayers etc
    }
  }

  /**
   * This method handles messages of the type NextLevel (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void nextLevel(@NotNull final NextLevel message) {
    currentGameState = message.gameState;
    until = 0;
    for (var mole : GameView.instance.shownMoles) {
      var newMole = Board.getMole(message.gameState, mole.mole.position);
      if (newMole == null) mole.kill();
      else mole.mole = newMole;
    }
    Utils.makeToast("Die nächste Ebene wurde erreicht.", false);
    for (var eliminatedPlayer : message.eliminatedPlayers) {
      // is this information even necessary when we can just get the new currentGameState?!
      // only to recognize if we are eliminated
      if (Utils.isThatUs(eliminatedPlayer)) {
        Utils.makeToast("Leider hast du deinen Letzten Maulwurf verlorn.", true);
        Utils.makeToast("Du bist aus dem Spiel ausgeschieden und schaust nun zu.", true);
      }
      //currentGameState.activePlayers = Utils.arrayRemove(currentGameState.activePlayers, eliminatedPlayer);
    }
  }

  /**
   * This method handles messages of the type NotAllowedError (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void notAllowedError(@NotNull final NotAllowedError message) {
    Utils.makeToast(message.description, true);
  }

  /**
   * This method handles messages of the type NotFoundError (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void notFoundError(@NotNull final NotFoundError message) {
    Utils.makeToast(message.description, true);
  }

  /**
   * The message is sent in the "down" Direction. The message must be sent directly in response to 'getOverview'.
   * The message must be sent directly in response to 'registerOverviewObserver'.
   * The message belongs to the Overview observer and can be sent at any time, while one is registered to it.
   * Server sends the current overview to a client. This Message contains information on running
   * games as well as on completed and planned games. Furthermore it includes the data about the
   * tournaments and their associated games (if existing).
   */
  private void overview(@NotNull final Overview message) {
    this.overview = message;
    LobbyActivity.overviewUpdated(message);
  }

  /**
   * The message is sent in the "down" Direction. The message belongs to the Game observer and can be sent at any time, while one is registered to it.
   * Server notifies clients that a new player has joined the game.
   */
  private void playerJoined(@NotNull final PlayerJoined message) {
    this.currentGameState.activePlayers = Utils.arrayPush(this.currentGameState.activePlayers, message.player);
    this.currentGameState.score.players = this.currentGameState.activePlayers;
    this.currentGameState.score.points.put(message.player.clientID, 0);
    this.currentGameState.pullDiscs.put(message.player.clientID, Utils.intToIntegerArray(game.pullDiscs));
    Utils.makeToast("Spieler '" + message.player.name + "' ist beigetreten!", true);
  }

  /**
   * The message is sent in the "down" Direction. The message belongs to the Game observer and can be sent at any time, while one is registered to it.
   * Server notifies clients that a player was kicked out of the game. This unregisters the player’s client as an observer of the game.
   */
  @Contract(pure = true)
  private void playerKicked(@NotNull final PlayerKicked message) {
    var player = message.player;
    Utils.makeToast("Der Spieler '" + player.name + "' hat das Spiel verlassen.", true);
    currentGameState.activePlayers = Utils.arrayRemovePlayer(currentGameState.activePlayers, player);
    currentGameState.score.players = Utils.arrayRemovePlayer(currentGameState.score.players, player);
    currentGameState.score.points.remove(message.player.clientID);
    currentGameState.pullDiscs.remove(message.player.clientID);
    for (var mole : GameView.instance.shownMoles)
      if (mole.mole.player.clientID == player.clientID) mole.kill();
    if (Utils.isThatUs(message.player)) {
      Utils.makeToast("Du wurdest aus diesem Spiel entfernt.", true);
      // TODO return to Lobby
    } else {
      Utils.makeToast("Spieler " + message.player.name + " wurde aus dem Spiel entfernt.", true);
      currentGameState.activePlayers = Utils.arrayRemovePlayer(currentGameState.activePlayers, message.player);
    }
  }

  /**
   * The message is sent in the "down" Direction. The message belongs to the Game observer and can be sent at any time, while one is registered to it.
   * Server notifies clients that a player has left the game.
   */
  private void playerLeft(@NotNull final PlayerLeft message) {
    var player = message.player;
    Utils.makeToast("Der Spieler '" + player.name + "' hat das Spiel verlassen.", true);
    currentGameState.activePlayers = Utils.arrayRemovePlayer(currentGameState.activePlayers, player);
    currentGameState.score.players = Utils.arrayRemovePlayer(currentGameState.score.players, player);
    currentGameState.score.points.remove(message.player.clientID);
    currentGameState.pullDiscs.remove(message.player.clientID);
    for (var mole : GameView.instance.shownMoles)
      if (mole.mole.player.clientID == player.clientID) mole.kill();
  }

  /**
   * This method handles messages of the type PlayerPlacesMole (defined by the Interface Document) send by the server.
   * @param message   The {@link Message} to be handled.
   */
  private void playerPlacesMole(PlayerPlacesMole message) {
    currentGameState.currentPlayer = message.player;
    until = message.until;

    if (Utils.isThatUs(message.player)) {
      Utils.makeToast("Platziere einen Maulwurf! " , true);

    } else {
      // is a message needed here? Could get pretty annoying when having a lot of friends
    }
  }
  /**
   * This method handles messages of the type placeMole (defined by the Interface Document) sent
   * by the client in up direction.
   * @param message   The {@link Message} to be handled.
   * @author Dila, Alp
   */
  public void placeMole(PlaceMole message) {

    if ((currentGameState.status == GameStatus.STARTED)) {
      if ((Utils.isThatUs(currentGameState.currentPlayer))) {
        Client.sendMessage(message);
      } else {
        Utils.makeToast("Du bist nicht dran! ", true);
      }
    }
    if (currentGameState.status == GameStatus.NOT_STARTED) {
      Utils.makeToast("Das Spiel hat noch nicht begonnen! ", true);
    }
  }
  /**
   * This method handles messages of the type makeMove (defined by the Interface Document) sent
   * by the client in up direction.
   * @param message   The {@link Message} to be handled.
   * @author Dila, Alp
   */
  public void makeMove(MakeMove message){
    if ((currentGameState.status == GameStatus.STARTED)) {
      if((Utils.isThatUs(currentGameState.currentPlayer))){
        Client.sendMessage(message);
      }
    }else {
      Utils.makeToast("Du bist nicht dran! " , true);
    }
  }

  /**
   * This method handles messages of the type PlayerSkipped (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void playerSkipped(@NotNull final PlayerSkipped message) {
    until = 0;
    Utils.makeToast("Spieler " + message.player.name + " hat hat diese Runde übersprungen.", false);
  }

  /**
   * The message is sent in the "down" Direction. The message belongs to the Game observer and can
   * be sent at any time, while one is registered to it.
   * Tell the client who’s turn it is.
   */
  private void playersTurn(@NotNull final PlayersTurn message) {
    currentGameState.currentPlayer = message.player;
    until = message.until;
    if (Utils.isThatUs(message.player)) {
      // when it's "our" turn
      // TODO return player to GameView when they're in (?)-menu to view the rules.
      Utils.makeToast("Du bist an der Reihe. Mach deinen Zug!", false);
    }
  }

  /**
   * The message is sent in the "down" Direction. The message must be sent directly in response to
   * getRemainingTime.
   * Sends the remaining turn time to the client.
   */
  private void remainingTime(@NotNull final RemainingTime message) {
    currentGameState.remainingTime = message.timeLeft;
  }

  /**
   * The message is sent in the "down" Direction. The message must be sent directly in response to
   * getScore.
   * Sends the current game score to the client.
   */
  private void scoreNotification(@NotNull final ScoreNotification message) {
    currentGameState.score = message.score;
  }

  /**
   * This method handles messages of the type TournamentGamesOverview (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void tournamentGamesOverview(@NotNull final TournamentGamesOverview message) {
  }

  /**
   * This method handles messages of the type TournamentOver (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void tournamentOver(@NotNull final TournamentOver message) {
  }

  /**
   * This method handles messages of the type TournamentPlayerInGame (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void tournamentPlayerInGame(@NotNull final TournamentPlayerInGame message) {
  }

  /**
   * This method handles messages of the type TournamentPlayerInLobby (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void tournamentPlayerInLobby(@NotNull final TournamentPlayerInLobby message) {
  }

  /**
   * The message is sent in the "down" Direction. The message belongs to the Tournament observer
   * and can be sent at any time, while one is registered to it.
   * Notify the client that a player joined the tournament.
   */
  private void tournamentPlayerJoined(@NotNull final TournamentPlayerJoined message) {
    tournamentState.players = Utils.arrayPush(tournamentState.players, message.player);
    Utils.makeToast("Spieler '" + message.player.name + "' ist dem Turnier beigetreten!", true);
  }

  /**
   * The message is sent in the "down" Direction. The message belongs to the Tournament observer
   * and can be sent at any time, while one is registered to it.
   * Notify the client that a player was kicked out of the tournament.
   * This will unregister the player’s client as an observer of the Tournament.
   */
  private void tournamentPlayerKicked(@NotNull final TournamentPlayerKicked message) {
    String kickNotification;
    if (!message.player.name.equals(Client.username)) {
      kickNotification = "Spieler " + message.player.name + " wurde aus dem Turnier entfernt.";
    } else {
      kickNotification = "Du wurdest aus diesem Turnier entfernt.";
    }
    LoginActivity.getInstance().runOnUiThread(() -> Toast.makeText(LoginActivity.getInstance(), kickNotification, Toast.LENGTH_LONG).show());
  }
  //TODO remove player from tournament

  /**
   * The message is sent in the "down" Direction. The message belongs to the Tournament observer
   * and can be sent at any time, while one is registered to it.
   * Notify the client that a player left the tournament.
   */
  private void tournamentPlayerLeft(@NotNull final TournamentPlayerLeft message) {
    var player = message.player;
    Utils.makeToast("Der Spieler '" + player.name + "' hat das Turnier verlassen.", true);
    tournamentState.players = Utils.arrayRemovePlayer(tournamentState.players, player);
    // TODO delete leaving player from Score PROBLEM with score, hashmap mapped to players position,
    //  TODo do keys in hashmap switch with players position in Array?
  }

  /**
   * This method handles messages of the type TournamentScore (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void tournamentScore(@NotNull final TournamentScore message) {
    tournamentState.score = message.score;
  }

  /**
   * This method handles messages of the type TournamentStateResponse (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void tournamentStateResponse(@NotNull final TournamentStateResponse message) {
    tournamentState = message.tournamentState;
  }

  /**
   * This method handles messages of the type UndefinedError (defined by the Interface Document) send by the server.
   *
   * @param message The {@link Message} to be handled.
   */
  private void undefinedError(@NotNull final UndefinedError message) {
    Utils.makeToast(message.description, true);
  }

  /**
   * The message is sent in the "down" Direction. The message will be responded by 'login'.
   * First message to a client telling the client that the connection has been established successfully and communication is working.
   */
  private void welcome(@NotNull final Welcome message) {
    Client.clientId = message.clientID;
    System.out.println("Clients id is: " + Client.clientId);
    //        Utils.makeToast("Clients id is: " + Client.clientId, true);

        /*
         The message is sent in the "up" Direction. The message must be sent directly in response to 'welcome'.
         Client sends login message containing the user’s name (mandatory for players, optional for spectators).
        */
    Client.sendMessages(new Login(Client.username),new RegisterOverviewObserver());
    Intent lobbyIntent = new Intent(Utils.getContext(), LobbyActivity.class);
    Utils.getContext().startActivity(lobbyIntent);
  }

  /**
   * The message is sent in the "down" Direction. The message must be sent directly in response to 'joinGame'.
   * This message is telling the client that he joined the game successfully and sends the current game state.
   */
  private void welcomeGame(@NotNull final WelcomeGame message) {
    if (this.game == null) {
      Utils.makeToast("Welcomed game not found", true);
      return;
    }
    this.currentGameState = message.gameState;
    if (GameView.instance != null) GameView.instance.reset(this.currentGameState);
    if (GameActivity.instance != null) return;
    var gameIntent = new Intent(Utils.getContext(), GameActivity.class);
    Utils.getContext().startActivity(gameIntent);
  }
  // TODO: Alle anderen Messages
  // --------------------------------- internal messages -----------------------------------------

  /**
   * Notify the Server that you want to join a Game.
   *
   * @param game        the Game you're joining
   * @param participant are you joining as an active Player?
   */
  public void joinGame(@NotNull final Game game, final boolean participant) {
    Client.sendMessages(new UnregisterOverviewObserver(),new JoinGame(game.gameID,    participant));
    this.overview = null;
    this.game = game;
  }

  public void leaveGame() {
    Client.sendMessage(new LeaveGame());
  }
}
