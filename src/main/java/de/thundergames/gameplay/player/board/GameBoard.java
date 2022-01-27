/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 17:01 by Carina Latest changes made by Carina on 20.01.22, 17:00 All contents of "GameBoard" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.gameplay.player.ui.score.LeaderBoard;
import de.thundergames.gameplay.player.ui.score.PlayerResult;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import de.thundergames.playmechanics.util.Punishments;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Getter
@Setter
public class GameBoard {

  private static Client CLIENT;
  private static GameBoard OBSERVER;
  private static HashMap<Integer, String> playersColors;
  private static BoardCountDown COUNTDOWN;
  private int BOARD_RADIUS;
  private Stage primaryStage;
  private BorderPane borderPane;
  private BorderPane countDownPane;
  private BorderPane turnPane;
  private BorderPane scorePane;
  private GameHandler gameHandler;
  private GameState gameState;

  private ObservableList<PlayerTable> resultList;

  private ScrollPane scrollPane;
  private TextFlow textFlow;

  private TableView<PlayerTable> playerListTable;
  private HashMap<Integer, ArrayList<Integer>> pullDiscs;
  private HashMap<Integer, ArrayList<Integer>> pullDiscsDefault;
  private Score score;

  private HashSet<Player> players;
  private ArrayList<PlayerModel> playerModelList;

  private double visTime;

  private boolean initialized = false;

  public static GameBoard getObserver() {
    return OBSERVER;
  }

  /**
   * @param primaryStage
   * @author Alp, Dila, Issam
   * @use starts the stage
   */
  public void create(Stage primaryStage) {
    OBSERVER = this;
    CLIENT = Client.getClientInstance();
    this.primaryStage = primaryStage;
    borderPane = new BorderPane();
    countDownPane = new BorderPane();
    countDownPane.setMinHeight(60);
    turnPane = new BorderPane();
    turnPane.setMinHeight(50);
    scorePane = new BorderPane();
    scorePane.setMinWidth(50);

    // get gameState
    gameState = CLIENT.getGameState();
    if (gameState == null) return;
    visTime = gameState.getVisualizationTime();
    if (visTime == 0.0) {
      visTime = 5;
    }
    //start timer of gameBoard
    COUNTDOWN = new BoardCountDown();
    COUNTDOWN.setTimer(!Objects.equals(gameState.getStatus(), GameStates.PAUSED.toString()));
    CLIENT.setRemainingTime(gameState.getRemainingTime());

    // get radius
    BOARD_RADIUS = gameState.getRadius();

    //get current player
    CLIENT.setCurrentPlayer(gameState.getCurrentPlayer());
    var currentPlayerID = gameState.getCurrentPlayer() == null ? -1 : CLIENT.getCurrentPlayer().getClientID();

    // create list of playerModels for ui
    players = gameState.getActivePlayers();
    var randomColorsItertator = Utils.getRandomHSLAColor(players.size()).listIterator();
    playersColors = new HashMap<>(players.stream().collect(Collectors.toMap(Player::getClientID, player -> randomColorsItertator.next())));
    var placedMoles = gameState.getPlacedMoles();
    var playerModelList = mapPlayersToPlayerModels(players, placedMoles, currentPlayerID, playersColors);

    // Set custom cursor
    var cursor = new Image(Utils.getSprite("game/cursor.png"));
    borderPane.setCursor(new ImageCursor(cursor,
      cursor.getWidth() / 2,
      cursor.getHeight() / 2));

    //Logout Button
    BorderPane LogoutPane = new BorderPane();
    BorderPane LogoutInnerPane = new BorderPane();
    AnchorPane stylePane = new AnchorPane();
    stylePane.setMinHeight(50);
    stylePane.setMinWidth(240);
    Button logoutButton = new Button();
    logoutButton.setOpacity(0);
    logoutButton.setMinHeight(45);
    logoutButton.setMinWidth(235);
    ImageView logoutButtonImage = new ImageView();
    logoutButtonImage.setImage(new Image(Objects.requireNonNull(Utils.class.getResource("/player/pictures/LogoutButton.png")).toString()));
    logoutButtonImage.setFitHeight(45);
    logoutButtonImage.setFitWidth(235);
    stylePane.getChildren().add(logoutButtonImage);
    stylePane.getChildren().add(logoutButton);
    LogoutInnerPane.setRight(stylePane);
    LogoutPane.setBottom(LogoutInnerPane);
    scorePane.setBottom(LogoutPane);
    logoutButton.setOnAction(
      e -> {
        try {
          backToMenu(e);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      });

    //window logout
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));

    //set root of panes
    var rootPane = new BorderPane();
    rootPane.setTop(countDownPane);
    rootPane.setCenter(borderPane);
    rootPane.setBottom(turnPane);
    rootPane.setRight(scorePane);

    scrollPane = new ScrollPane();
    textFlow = new TextFlow();
    textFlow.setStyle("-fx-background-color: rgba(65, 23, 167, 1);");
    scrollPane.setContent(textFlow);
    turnPane.setCenter(scrollPane);
    turnPane.setMinHeight(100);
    turnPane.setMaxHeight(100);
    scrollPane.setMaxHeight(turnPane.getMaxHeight());
    scrollPane.setMinHeight(turnPane.getMinHeight());
    // Create a game handler and add random players to it
    gameHandler = new GameHandler(playerModelList, BOARD_RADIUS, updateFloor(gameState), borderPane, rootPane);
    gameHandler.start(playerModelList);
    // Add resize event listener
    var resizeObserver = (ChangeListener<Number>) (obs, newValue, oldValue) -> gameHandler.getBoard().onResize(borderPane.getWidth(), borderPane.getHeight());
    borderPane.widthProperty().addListener(resizeObserver);
    borderPane.heightProperty().addListener(resizeObserver);
    // Add board to center of borderPane
    borderPane.setCenter(gameHandler.getBoard());
    updateScoreTable();
    rootPane.setMinHeight(856.0);
    rootPane.setMinWidth(900.0);
    var s = new Scene(rootPane);
    s.getStylesheets().add("/player/style/css/GameBoard.css");
    scrollPane.setId("gamelog");
    updateRemainingTime();
    primaryStage.setScene(s);
    primaryStage.setResizable(true);
    primaryStage.setMaximized(true);
    primaryStage.show();
    initialized = true;
    CLIENT.getClientPacketHandler().getRemainingTimePacket();
  }

  //window logout
  private void logout(Stage stage) {
    CLIENT.getClientPacketHandler().logoutPacket();
    stage.close();
  }

  private void backToMenu(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Spiel verlassen");
    alert.setHeaderText("");
    alert.setContentText("Wollen Sie das Spiel verlassen?");
    ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    ButtonType noButton = new ButtonType("NO", ButtonBar.ButtonData.NO);
    alert.getButtonTypes().setAll(okButton, noButton);
    alert.showAndWait().ifPresent(type -> {
      if (type.getButtonData().name().equals(ButtonType.YES.getButtonData().name()))
      {
        COUNTDOWN.deleteTimer();
        CLIENT.getClientPacketHandler().leaveGamePacket();
        try {
          new PlayerMenu().create(event);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public ArrayList<PlayerModel> mapPlayersToPlayerModels(@NotNull final HashSet<Player> players, @NotNull final HashSet<Mole> placedMoles, final int currentPlayerID, @NotNull final HashMap<Integer, String> playersColors) {
    var playerModelList = new ArrayList<PlayerModel>();
    for (var player : players) {
      var moleModelList = new ArrayList<MoleModel>();
      for (var mole : placedMoles) {
        if (player.getClientID() == mole.getPlayer().getClientID()) {
          moleModelList.add(new MoleModel(player.getClientID(), mole, playersColors.get(player.getClientID())));
        }
      }
      playerModelList.add(new PlayerModel(player, moleModelList, player.getClientID() == currentPlayerID, playersColors.get(player.getClientID())));
    }
    return playerModelList;
  }

  public void updateGameBoard() {
    var loadedGameState = CLIENT.getGameState();
    if (gameState != loadedGameState) {
      //Update board if count of holes changed
      if (gameState.getFloor().getHoles().size() != loadedGameState.getFloor().getHoles().size()) {
        var nodes = updateFloor(loadedGameState);
        gameHandler.setNodeTypes(nodes);
        var backgroundList = new ArrayList<>(List.of("background/ug_1.png", "background/ug_2.png", "background/ug_3.png"));
        backgroundList.remove(gameHandler.getBackground());
        gameHandler.setBackground(backgroundList.get(new Random().nextInt(backgroundList.size() - 1)));
      }
      gameState = loadedGameState;
      // get active players of gameState
      players = gameState.getActivePlayers();
    }
    //get current player
    var currentPlayerID = CLIENT.getCurrentPlayer() == null ? -1 : CLIENT.getCurrentPlayer().getClientID();
    var currentPlayerName = CLIENT.getCurrentPlayer() == null ? "" : CLIENT.getCurrentPlayer().getName();
    //get moles
    var fieldMap = CLIENT.getMap().getFieldMap();
    var placedMoles = new HashSet<Mole>();
    for (var field : fieldMap.values()) {
      var currentMole = field.getMole();
      if (currentMole != null) {
        if (currentMole.getPosition().getX() != field.getX() || currentMole.getPosition().getY() != field.getY()) {
          currentMole.setPosition(field);
          System.out.println(currentMole.getPosition().getX() + " " + currentMole.getPosition().getY() + "/ " + field.getX() + " " + field.getY());
        }
        placedMoles.add(currentMole);
      }
    }
    playerModelList = mapPlayersToPlayerModels(players, placedMoles, currentPlayerID, playersColors);
    gameHandler.update(playerModelList);
    CLIENT.getClientPacketHandler().getRemainingTimePacket();
  }

  public void updateScoreTable() {
    Platform.runLater(() -> {
      playerListTable = new TableView<>();
      playerListTable.setEditable(false);
      @SuppressWarnings("rawtypes") var placeColumn = new TableColumn("Platz");
      placeColumn.setMinWidth(10);
      placeColumn.setCellValueFactory(
        new PropertyValueFactory<PlayerResult, Integer>("placement"));
      @SuppressWarnings("rawtypes") var nameColumn = new TableColumn("Name");
      nameColumn.setMinWidth(30);
      nameColumn.setCellValueFactory(
        new PropertyValueFactory<PlayerResult, String>("name"));
      @SuppressWarnings("rawtypes") var pointsColumn = new TableColumn("Punkte");
      pointsColumn.setMinWidth(10);
      pointsColumn.setCellValueFactory(
        new PropertyValueFactory<PlayerResult, Integer>("score"));
      @SuppressWarnings("rawtypes") var pullDiscsColumn = new TableColumn("Karten");
      pullDiscsColumn.setMinWidth(10);
      pullDiscsColumn.setCellValueFactory(
        new PropertyValueFactory<PlayerResult, Integer>("pullDiscs"));
      ObservableList<PlayerTable> newResultList = FXCollections.observableArrayList();
      var score = CLIENT.getGameState().getScore();
      Gson gson = new Gson();
      String jsonString = gson.toJson(CLIENT.getGameState().getPullDiscs());
      Type type = new TypeToken<HashMap<Integer, ArrayList<Integer>>>() {
      }.getType();
      pullDiscs = gson.fromJson(jsonString, type);
      if (pullDiscsDefault == null) {
        pullDiscsDefault = gson.fromJson(jsonString, type);
      }
      var thisPlace = 1;
      var players = score.getPlayers();
      var size = score.getPlayers().size();
      var highestScore = 0;
      Player highestPlayer = null;
      while (newResultList.size() != size) {
        var firstValue = true;
        for (var player : players) {
          var playerScore = 0;
          if (score.getPoints().get(player.getClientID()) != null) {
            playerScore = score.getPoints().get(player.getClientID());
          }
          if (firstValue) {
            highestScore = playerScore;
            highestPlayer = player;
            firstValue = false;
          } else {
            if (highestScore < playerScore) {
              highestScore = playerScore;
              highestPlayer = player;
            }
          }
        }
        var playerName = Integer.toString(highestPlayer.getClientID());
        if (highestPlayer.getName() != null) {
          playerName = playerName + "/" + highestPlayer.getName();
        }
        newResultList.add(
          new PlayerTable(playerName, highestScore, thisPlace, pullDiscs.get(highestPlayer.getClientID()).toString()));
        players.remove(highestPlayer);
        highestScore = -1;
        highestPlayer = null;
        thisPlace++;
      }
      if (resultList != newResultList && !newResultList.isEmpty()) {
        resultList = newResultList;
      }
      playerListTable.setItems(resultList);
      playerListTable.getColumns().addAll(placeColumn, nameColumn, pointsColumn, pullDiscsColumn);
      playerListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      playerListTable.prefHeightProperty().bind(primaryStage.heightProperty());
      playerListTable.getSortOrder().add(placeColumn);
      scorePane.setCenter(playerListTable);
    });
  }

  public void updatePullDiscs(Player player, Integer pullDisc) {
    for (PlayerTable playerTable : resultList) {
      if (playerTable.getName().equals(player.getClientID() + "/" + player.getName())) {
        pullDiscs.get(player.getClientID()).remove(pullDisc);
        if (pullDiscs.get(player.getClientID()).size() == 0) {
          pullDiscs.get(player.getClientID()).addAll(pullDiscsDefault.get(player.getClientID()));
        }
        playerTable.setPullDiscs(pullDiscs.get(player.getClientID()).toString());
        break;
      }
    }
    playerListTable.refresh();
  }

  public void updateGameLog(Integer playerID, String playerName, String information) {
    Platform.runLater(() -> {
      var playerString = Integer.toString(playerID);
      if (!playerName.equals("")) {
        playerString = playerString + "/" + playerName;
      }
      var playerText = new Text(playerString);
      var beginning = new Text("\u25B9 Spieler ");
      var end = new Text(information);
      var defTextColor = "#ffffff";
      beginning.setId("text");
      beginning.setFill(Paint.valueOf(defTextColor));
      end.setId("text");
      end.setFill(Paint.valueOf(defTextColor));
      playerText.setId("text");
      playerText.setFill(Paint.valueOf(playersColors.get(playerID)));
      playerText.setStroke(Paint.valueOf(defTextColor));
      playerText.setStrokeWidth(0.5);
      textFlow.getChildren().addAll(beginning, playerText, end);
      scrollPane.setVvalue(1.0f);
    });
  }

  public HashMap<List<Integer>, NodeType> updateFloor(@NotNull final GameState gameState) {
    var nodes = new HashMap<List<Integer>, NodeType>();
    gameState.getFloor().getHoles().forEach(field -> nodes.put(List.of(field.getX(), field.getY()), NodeType.HOLE));
    gameState.getFloor().getDrawAgainFields().forEach(field -> nodes.put(List.of(field.getX(), field.getY()), NodeType.DRAW_AGAIN));
    return nodes;
  }

  public void updateRemainingTime() {
    Platform.runLater(() -> {
      if (CLIENT.getRemainingTime() > 0) {
        long time = CLIENT.getRemainingTime();
        COUNTDOWN.setRemainingTime(time);
        updateTime(time, COUNTDOWN.getShowCount());
      }
    });
  }

  public void updateRemainingDateTime()
  {
    Platform.runLater(() -> {
      if (CLIENT.getRemainingDateTime() > 0) {
        long time = CLIENT.getRemainingDateTime() - System.currentTimeMillis();
        COUNTDOWN.setRemainingTime(time);
        updateTime(time, COUNTDOWN.getShowCount());
      }
    });
  }

  public void stopRemainingTime()
  {
    Platform.runLater(() -> {
      COUNTDOWN.setStopTurnOver(true);
    });
  }

  public void continueRemainingTime()
  {
    Platform.runLater(() -> {
    COUNTDOWN.setStopTurnOver(false);
    });
  }

  public void updateTime(long remainingTime, boolean run) {
    Platform.runLater(() -> {
      float remainingTimeInSec = (float) remainingTime / (float) 1000;
      var roundUpTime = (int) Math.ceil(remainingTimeInSec);
      Text txtRemainingTime = (run)
        ? new Text(String.valueOf(roundUpTime))
        : new Text("Das Spiel wurde pausiert!");
      var containerTimer = new AnchorPane();
      txtRemainingTime.setId("textTime");
      txtRemainingTime.setFill(Paint.valueOf(playersColors.get(CLIENT.getCurrentPlayer().getClientID())));
      containerTimer.getChildren().add(txtRemainingTime);
      countDownPane.setTop(txtRemainingTime);
      BorderPane.setAlignment(txtRemainingTime, Pos.TOP_CENTER);
    });
  }

  public void stopCountAfterTurn() {
    COUNTDOWN.stopCountAfterTurn();
  }

  public void checkForStopTimer() {
    COUNTDOWN.checkForStopTimer();
  }

  public void continueTimer() {
    COUNTDOWN.continueTimer();
  }

  public void showPenalty(Player player, String penalty, String reason, String deductedPoints) {
    var out = "";
    if (Objects.equals(reason, Punishments.INVALIDMOVE.toString())) {
      out = " hat einen fehlerhaften Zug gemacht.";
    } else if (Objects.equals(reason, Punishments.NOMOVE.toString())) {
      out = " hat die Spielzeit überschritten.";
    }
    if (Objects.equals(penalty, Punishments.POINTS.toString())) {
      out += " " + deductedPoints + " Punkte wurden dem Spieler entzogen. ";
      CLIENT.getClientPacketHandler().getScorePacket();
    }
    if (Objects.equals(penalty, Punishments.KICK.toString())) {
      out += " Spieler wurde gekickt.";
    }
    out += "\n";
    updateGameLog(player.getClientID(), player.getName(), out);
  }

  public void moveMole(Field from, Field to, int currentPlayerId, int pullDisc) {
    Platform.runLater(() -> this.gameHandler.getBoard().moveMole(from, to, currentPlayerId, pullDisc, visTime));
  }

  public void placeMole(Mole mole) {
    Platform.runLater(() -> this.gameHandler.getBoard().placeMole(new MoleModel(mole, playersColors.get(mole.getPlayer().getClientID())), visTime));
  }

  public void kickMolesOfPlayer(Player player) {
    Platform.runLater(() -> this.gameHandler.getBoard().removePlayer(player));
  }

  public void gameOver(Score score) {
    Platform.runLater(() -> {
      LeaderBoard leaderBoard = new LeaderBoard();
      leaderBoard.create(score);
      try {
        leaderBoard.start(primaryStage);
      } catch (Exception e) {
        System.out.println(e);
      }
    });
  }
}
