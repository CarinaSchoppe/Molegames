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

import de.thundergames.gameplay.player.Client;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class GameBoard implements Initializable {

  private static Client CLIENT = null;
  private static GameBoard GAME_BOARD = null ;

  static int BOARD_RADIUS = 3;

  /**
   * @param primaryStage
   * @author Alp, Dila, Issam
   * @use starts the stage
   */
  public void create(Stage primaryStage) {

    var gameState = CLIENT.getGameState();
    if (gameState== null) return;

    var borderPane = new BorderPane();
    var test = gameState.getMoles();
    var test1 = gameState.getFloor();
    test1.getFieldMap();

    BOARD_RADIUS = gameState.getRadius();
    var players = gameState.getActivePlayers();




    // Set custom cursor
    var cursor = new Image(Utils.getSprite("game/cursor.png"));
    borderPane.setCursor(new ImageCursor(cursor,
      cursor.getWidth() / 2,
      cursor.getHeight() / 2));
    var maxPossibleID = 3 * (int) Math.pow(BOARD_RADIUS, 2) + 3 * BOARD_RADIUS + 1;
    // Create a game handler and add random players to it
    var nodeTypes = generateRandomNodeTypes(50);

    var gameHandler = new GameHandler(players, BOARD_RADIUS, nodeTypes);
    gameHandler.start(borderPane);
    // Add resize event listener
    ChangeListener<Number> resizeObserver = (obs, newValue, oldValue) -> gameHandler.getBoard().onResize(borderPane.getWidth(), borderPane.getHeight());
    borderPane.widthProperty().addListener(resizeObserver);
    borderPane.heightProperty().addListener(resizeObserver);
    // Add board to center of borderPane
    borderPane.setCenter(gameHandler.getBoard());
    var s = new Scene(borderPane);
    primaryStage.setScene(s);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }
  /**
   * @param numMoles
   * @param minId
   * @param maxId
   * @return the moles
   * @author Issam, Dila, Alp
   * @use generates random moles
   */
  public ArrayList<MoleModel> generateRandomMoles(final int numMoles, final int minId, final int maxId) {
    var moles = new ArrayList<MoleModel>();
    var randomIntsArray = IntStream.generate(() -> new Random().nextInt(maxId - minId + 1) + minId).limit(numMoles).toArray();
    for (var id : randomIntsArray) {
      moles.add(new MoleModel(id, 40));
    }
    return moles;
  }

  public ArrayList<PlayerModel> generateThreePlayers() {
    var players = new ArrayList<PlayerModel>();
    players.add(new PlayerModel(1, generateRandomMoles(3, 1, 7)));
    players.add(new PlayerModel(2, generateRandomMoles(3, 8, 13)));
    players.add(new PlayerModel(3, generateRandomMoles(3, 14, 19)));
    return players;
  }

  /**
   * @param size
   * @return the nodes
   * @author Issam, Dila, Alp
   * @use generates random node types
   */
  public ArrayList<NodeType> generateRandomNodeTypes(final int size) {
    var nodeTypes = new ArrayList<NodeType>();
    for (var i = 1; i <= size; i++) {
      nodeTypes.add(randomValueFromArray(NodeType.values()));
    }
    return nodeTypes;
  }

  <T> T randomValueFromArray(@NotNull final T[] values) {
    return values[new Random().nextInt(values.length)];
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    GAME_BOARD = this;
    CLIENT = Client.getClientInstance();
  }
}
