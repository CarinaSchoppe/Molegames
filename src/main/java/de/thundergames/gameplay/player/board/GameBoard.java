/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:35 by Carina Latest changes made by Carina on 09.01.22, 21:35 All contents of "GameBoard" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.score.PlayerResult;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class GameBoard implements Initializable {

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

  private ObservableList<PlayerResult> resultList;

  private ScrollPane scrollPane;
  private TextFlow textFlow;

  private Score score;

  private HashSet<Player> players;
  private ArrayList<PlayerModel> playerModelList;

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
    countDownPane.setMinHeight(50);
    turnPane = new BorderPane();
    turnPane.setMinHeight(50);
    scorePane = new BorderPane();
    scorePane.setMinWidth(50);
    // get gameState
    gameState = CLIENT.getGameState();
    if (gameState == null) return;
    //start timer of gameBoard
    COUNTDOWN = new BoardCountDown();
    COUNTDOWN.setTimer(!Objects.equals(gameState.getStatus(), GameStates.PAUSED.toString()));
    // get radius
    BOARD_RADIUS = gameState.getRadius();
    //get current player
    var currentPlayerId = gameState.getCurrentPlayer() == null ? -1 : gameState.getCurrentPlayer().getClientID();
    var currentPlayerName = CLIENT.getCurrentPlayer() == null ? "" : CLIENT.getCurrentPlayer().getName();
    // create list of playerModels for ui
    players = gameState.getActivePlayers();
    playersColors = new HashMap<>(players.stream().collect(Collectors.toMap(Player::getClientID, player -> Utils.getRandomHSLAColor())));
    var placedMoles = gameState.getPlacedMoles();
    var playerModelList = mapPlayersToPlayerModels(players, placedMoles, currentPlayerId, playersColors);
    // Set custom cursor
    var cursor = new Image(Utils.getSprite("game/cursor.png"));
    borderPane.setCursor(new ImageCursor(cursor,
            cursor.getWidth() / 2,
            cursor.getHeight() / 2));
    var rootPane = new BorderPane();
    rootPane.setTop(countDownPane);
    rootPane.setCenter(borderPane);
    rootPane.setBottom(turnPane);
    rootPane.setRight(scorePane);
    scrollPane = new ScrollPane();
    textFlow = new TextFlow();
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
    CLIENT.getClientPacketHandler().getRemainingTimePacket();
    updatePlayerList();
    if (currentPlayerId != -1) {
      playerTurnInformation(currentPlayerId, currentPlayerName);
    }
    var s = new Scene(rootPane);
    s.getStylesheets().add("/player/style/css/GameBoard.css");
    primaryStage.setScene(s);
    primaryStage.setResizable(true);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }

  public ArrayList<PlayerModel> mapPlayersToPlayerModels(@NotNull final HashSet<Player> players, @NotNull final HashSet<Mole> placedMoles, final int currentPlayerId, @NotNull final HashMap<Integer, String> playersColors) {
    var playerModelList = new ArrayList<PlayerModel>();
    for (var player : players) {
      var moleModelList = new ArrayList<MoleModel>();
      for (var mole : placedMoles) {
        if (player.getClientID() == mole.getPlayer().getClientID()) {
          moleModelList.add(new MoleModel(player.getClientID(), mole, playersColors.get(player.getClientID())));
        }
      }
      playerModelList.add(new PlayerModel(player, moleModelList, player.getClientID() == currentPlayerId, playersColors.get(player.getClientID())));
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
    var currentPlayerId = CLIENT.getCurrentPlayer() == null ? -1 : CLIENT.getCurrentPlayer().getClientID();
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
    playerModelList = mapPlayersToPlayerModels(players, placedMoles, currentPlayerId, playersColors);
    gameHandler.update(playerModelList);
    CLIENT.getClientPacketHandler().getRemainingTimePacket();
    if (currentPlayerId != -1) {
      playerTurnInformation(currentPlayerId, currentPlayerName);
    }
    updatePlayerList();
  }

  public void updatePlayerList() {
    Platform.runLater(() -> {
      var playerListTable = new TableView<PlayerResult>();
      playerListTable.setEditable(false);
      var placeColumn = new TableColumn("Platz");
      placeColumn.setMinWidth(10);
      placeColumn.setCellValueFactory(
              new PropertyValueFactory<PlayerResult, Integer>("placement"));
      var nameColumn = new TableColumn("Name");
      nameColumn.setMinWidth(30);
      nameColumn.setCellValueFactory(
              new PropertyValueFactory<PlayerResult, String>("name"));
      var pointsColumn = new TableColumn("Punkte");
      pointsColumn.setMinWidth(10);
      pointsColumn.setCellValueFactory(
              new PropertyValueFactory<PlayerResult, Integer>("score"));
      CLIENT.getClientPacketHandler().getScorePacket();
      ObservableList<PlayerResult> newResultList = FXCollections.observableArrayList();
      var newGameState = CLIENT.getGameState();
      if (gameState != newGameState) {
        gameState = newGameState;
      }
      if (score != gameState.getScore() && !gameState.getScore().getPlayers().isEmpty()) {
        score = gameState.getScore();
        var thisPlace = 1;
        for (var player : score.getPlayers()) {
          newResultList.add(
                  new PlayerResult(
                          player.getName(), score.getPoints().get(player.getClientID()), thisPlace));
          thisPlace++;
        }
      }
      if (resultList != newResultList && !newResultList.isEmpty()) {
        resultList = newResultList;
      }
      playerListTable.setItems(resultList);
      playerListTable.getColumns().addAll(placeColumn, nameColumn, pointsColumn);
      playerListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      playerListTable.prefHeightProperty().bind(primaryStage.heightProperty());
      scorePane.setCenter(playerListTable);
    });
  }

  public void playerTurnInformation(final int playerID, @NotNull final String playerName) {
    Platform.runLater(() -> {
      var playerString = Integer.toString(playerID);
      if (!playerName.equals("")) {
        playerString = playerString + "/" + playerName;
      }
      var playerText = new Text(playerString);
      var beginning = new Text("Spieler ");
      var end = new Text(" ist jetzt an der Reihe.\n");
      var defTextColor = "#ffffff";
      beginning.setId("text");
      beginning.setFill(Paint.valueOf(defTextColor));
      end.setId("text");
      end.setFill(Paint.valueOf(defTextColor));
      playerText.setId("text");
      playerText.setFill(Paint.valueOf(playersColors.get(playerID)));
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
      long remainingTime = CLIENT.getRemainingTime();
      var sys = System.currentTimeMillis();
      long time = remainingTime - sys;
      updateTime(time);
      COUNTDOWN.setRemainingTime(time);
    });
  }

  public void updateTime(final long remainingTime) {
    Platform.runLater(() -> {
      var txtRemainingTime = new Text(String.valueOf((remainingTime / 1000)));
      var container = new AnchorPane();
      txtRemainingTime.setFont(new javafx.scene.text.Font("Chicle", 50));
      container.getChildren().add(txtRemainingTime);
      countDownPane.setTop(txtRemainingTime);
      BorderPane.setAlignment(txtRemainingTime, Pos.TOP_CENTER);
    });
  }

  public void stopTimer() {
    COUNTDOWN.stopTimer();
  }

  public void continueTimer() {
    COUNTDOWN.continueTimer();
  }
}
