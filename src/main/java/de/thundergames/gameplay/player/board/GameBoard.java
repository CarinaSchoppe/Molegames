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
import de.thundergames.playmechanics.map.Field;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameBoard implements Initializable {

  private static Client CLIENT;
  private static GameBoard GAME_BOARD;

  static int BOARD_RADIUS = 3;

  /**
   * @param primaryStage
   * @author Alp, Dila, Issam
   * @use starts the stage
   */
  public void create(Stage primaryStage) {
    GAME_BOARD = this;
    CLIENT = Client.getClientInstance();

    var gameState = CLIENT.getGameState();
    if (gameState== null) return;

    var borderPane = new BorderPane();


    BOARD_RADIUS = gameState.getRadius();

    var players = gameState.getActivePlayers();
    ArrayList<PlayerModel> playerModels = new ArrayList<>();
    Random rn = new Random();
    for (var player:players) {

      var test = new HashSet<Mole>();
      test.add(new Mole(player,new Field(1,3)));
      player.setMoles(test);

      playerModels.add(new PlayerModel(player));
    }



    //Convert list of holes and drawAgainFields to list of object nodeType
    HashMap<List<Integer>, NodeType> nodes = new HashMap<>();
    gameState.getFloor().getHoles().forEach(field -> nodes.put(List.of(field.getX(),field.getY()), NodeType.HOLE));
    gameState.getFloor().getDrawAgainFields().forEach(field -> nodes.put(List.of(field.getX(),field.getY()), NodeType.DRAW_AGAIN));


















    // Set custom cursor
    var cursor = new Image(Utils.getSprite("game/cursor.png"));
    borderPane.setCursor(new ImageCursor(cursor,
      cursor.getWidth() / 2,
      cursor.getHeight() / 2));
    var maxPossibleID = 3 * (int) Math.pow(BOARD_RADIUS, 2) + 3 * BOARD_RADIUS + 1;
    // Create a game handler and add random players to it

    var gameHandler = new GameHandler(playerModels, BOARD_RADIUS, nodes);
    gameHandler.start(borderPane);
    // Add resize event listener
    ChangeListener<Number> resizeObserver = (obs, newValue, oldValue) -> gameHandler.getBoard().onResize(borderPane.getWidth(), borderPane.getHeight());
    borderPane.widthProperty().addListener(resizeObserver);
    borderPane.heightProperty().addListener(resizeObserver);
    // Add board to center of borderPane
    borderPane.setCenter(gameHandler.getBoard());
    var s = new Scene(borderPane);
    primaryStage.setScene(s);
    primaryStage.setResizable(true);
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}
