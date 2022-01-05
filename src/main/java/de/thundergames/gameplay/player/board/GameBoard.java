/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "TestWindow" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class GameBoard implements Initializable {

  private static Client CLIENT;
  private static GameBoard OBSERVER;

  private int BOARD_RADIUS;

  private Stage primaryStage;
  private BorderPane borderPane;
  private BorderPane countDownPane;
  private GameHandler gameHandler;
  private static String[] playersColors;

  public static GameBoard getObserver() {
    return OBSERVER;
  }

  private GameState gameState;

  private ObservableList<PlayerResult> resultList;

  private Score score;

  private HashSet<Player> players;
  private ArrayList<PlayerModel> playerModelList;

  private static BoardCountDown COUNTDOWN;
  /**
   * @param primaryStage
   * @author Alp, Dila, Issam
   * @use starts the stage
   */
  public void create(Stage primaryStage) throws InterruptedException {
    OBSERVER = this;
    CLIENT = Client.getClientInstance();
    this.primaryStage = primaryStage;
    borderPane = new BorderPane();
    countDownPane = new BorderPane();
    countDownPane.setMinHeight(50);
    // get gameState
    gameState = CLIENT.getGameState();
    if (gameState == null) return;
    //start timer of gameBoard
    COUNTDOWN = new BoardCountDown();
    COUNTDOWN.setTimer(!Objects.equals(gameState.getStatus(), GameStates.PAUSED.toString()));
    // get radius
    BOARD_RADIUS = gameState.getRadius();

    //get current player
    var currentPlayerId = gameState.getCurrentPlayer()== null  ? -1 : gameState.getCurrentPlayer().getClientID();

    // create list of playerModels for ui
   players = gameState.getActivePlayers();
    playersColors = players.stream().map(player -> Utils.getRandomHSLAColor()).toArray(String[]::new);
    HashSet<Mole> placedMoles = gameState.getPlacedMoles();
    var playerModelList = mapPlayersToPlayerModels(players, placedMoles, currentPlayerId, playersColors);
    // Set custom cursor
    var cursor = new Image(Utils.getSprite("game/cursor.png"));
    borderPane.setCursor(new ImageCursor(cursor,
      cursor.getWidth() / 2,
      cursor.getHeight() / 2));
    var rootPane = new BorderPane();
    rootPane.setTop(countDownPane);
    rootPane.setCenter(borderPane);
    // Create a game handler and add random players to it
    gameHandler = new GameHandler(playerModelList, BOARD_RADIUS, updateFloor(gameState), borderPane, rootPane);
    gameHandler.start(playerModelList);
    // Add resize event listener
    ChangeListener<Number> resizeObserver = (obs, newValue, oldValue) -> gameHandler.getBoard().onResize(borderPane.getWidth(), borderPane.getHeight());
    borderPane.widthProperty().addListener(resizeObserver);
    borderPane.heightProperty().addListener(resizeObserver);
    // Add board to center of borderPane
    borderPane.setCenter(gameHandler.getBoard());
    CLIENT.getClientPacketHandler().getRemainingTimePacket();
    updatePlayerList();
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

  public ArrayList<PlayerModel> mapPlayersToPlayerModels(HashSet<Player> players, HashSet<Mole> placedMoles, Integer currentPlayerId,String[] playersColors) {
    ArrayList<PlayerModel> playerModelList = new ArrayList<>();
    for (var player : players) {
      ArrayList<MoleModel> moleModelList = new ArrayList<>();
      for (var mole : placedMoles) {
        if (player.getClientID() == mole.getPlayer().getClientID()) {
          moleModelList.add(new MoleModel(player.getClientID(),mole, playersColors[player.getClientID()]));
        }
      }
      playerModelList.add(new PlayerModel(player,moleModelList,player.getClientID() == currentPlayerId, playersColors[player.getClientID()]));
    }
    return playerModelList;
  }


  public void updateGameBoard()
  {
    var loadedGameState = CLIENT.getGameState();

    if (gameState != loadedGameState)
    {
      //Update board if count of holes changed
      if (gameState.getFloor().getHoles().size() != loadedGameState.getFloor().getHoles().size())
      {
        HashMap<List<Integer>, NodeType> nodes;
          nodes = updateFloor(loadedGameState);
          gameHandler.setNodeTypes(nodes);
          ArrayList<String> backgroundList = new ArrayList<>( List.of("background/ug_1.png","background/ug_2.png","background/ug_3.png"));
          backgroundList.remove(gameHandler.getBackground());
          gameHandler.setBackground(backgroundList.get(new Random().nextInt(backgroundList.size()-1)));
      }
      gameState=loadedGameState;


      // get active players of gameState
      players = gameState.getActivePlayers();
    }

    //get current player
    var currentPlayerId = CLIENT.getCurrentPlayer() == null ? -1 : CLIENT.getCurrentPlayer().getClientID();
    var currentPlayerName = CLIENT.getCurrentPlayer() == null ? "" : CLIENT.getCurrentPlayer().getName();
    //get moles
    var fieldMap = CLIENT.getMap().getFieldMap();
    HashSet<Mole> placedMoles = new HashSet<>();
    for (var field :fieldMap.values())
    {
      var currentMole = field.getMole();
      if (currentMole != null) {
        if (currentMole.getField().getX() != field.getX() || currentMole.getField().getY() != field.getY()) {
          currentMole.setField(field);
          System.out.println(currentMole.getField().getX() + " " + currentMole.getField().getY() + "/ " + field.getX() + " " + field.getY());
        }
        placedMoles.add(currentMole);
      }
    }


    playerModelList = mapPlayersToPlayerModels(players, placedMoles, currentPlayerId,playersColors);
    gameHandler.update(playerModelList);
    CLIENT.getClientPacketHandler().getRemainingTimePacket();

    if (!currentPlayerName.equals("")) {
      playerTurnInformation(currentPlayerName);
    }
    updatePlayerList();
  }

  public void updatePlayerList() {
    Platform.runLater(() -> {
      TableView<PlayerResult> playerListTable = new TableView<>();
      playerListTable.setEditable(false);

      TableColumn placeColumn = new TableColumn("Platz");
      placeColumn.setMinWidth(10);
      placeColumn.setCellValueFactory(
              new PropertyValueFactory<PlayerResult, Integer>("placement"));

      TableColumn nameColumn = new TableColumn("Name");
      nameColumn.setMinWidth(30);
      nameColumn.setCellValueFactory(
              new PropertyValueFactory<PlayerResult, String>("name"));

      TableColumn pointsColumn = new TableColumn("Punkte");
      pointsColumn.setMinWidth(10);
      pointsColumn.setCellValueFactory(
              new PropertyValueFactory<PlayerResult, Integer>("score"));

      CLIENT.getClientPacketHandler().getScorePacket();
      ObservableList<PlayerResult> newResultList = FXCollections.observableArrayList();
      var newGameState = CLIENT.getGameState();
      if (gameState != newGameState) {
        gameState = newGameState;
      }
      System.out.println(gameState.getScore());
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
      //playerListTable.setStyle("-fx-background-color: -fx-table-cell-border-color, rgba(65, 23, 167, 1);");
      playerListTable.prefHeightProperty().bind(primaryStage.heightProperty());
      var container = new BorderPane();
      container.setRight(playerListTable);
      BorderPane.setAlignment(playerListTable, Pos.CENTER_RIGHT);
      borderPane.setRight(container);
    });
  }

  public void playerTurnInformation(String playerName) {
    Platform.runLater(() -> {
      var notification = new Text("Spieler " + playerName + " ist jetzt an der Reihe.");
      var container = new BorderPane();
      notification.setId("text");
      container.setTop(notification);
      BorderPane.setAlignment(notification, Pos.BOTTOM_CENTER);
      borderPane.setBottom(container);
    });
  }

  public HashMap<List<Integer>, NodeType> updateFloor(GameState gameState) {
    HashMap<List<Integer>, NodeType> nodes = new HashMap<>();
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

  public void updateTime(long remainingTime)
  {
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

  public void continueTimer(){
    COUNTDOWN.continueTimer();
  }
}
