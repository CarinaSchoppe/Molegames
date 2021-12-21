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
import de.thundergames.gameplay.player.ui.gameselection.GameSelection;
import de.thundergames.gameplay.player.ui.gameselection.LobbyObserverGame;
import de.thundergames.playmechanics.game.GameState;
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
  private static GameBoard OBSERVER;

  private int BOARD_RADIUS;

  private Stage primaryStage;
  private BorderPane borderPane;
  private GameHandler gameHandler;

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

    // get gameState
    var gameState = CLIENT.getGameState();
    if (gameState== null) return;

    // get radius
    BOARD_RADIUS = gameState.getRadius();

    //get current player
    var currentPlayerId = gameState.getCurrentPlayer()== null  ? -1 : gameState.getCurrentPlayer().getClientID();

    // create list of playerModels for ui
   ArrayList<Player> players = gameState.getActivePlayers();
   ArrayList<Mole> placedMoles = gameState.getPlacedMoles();
    var playerModelList = mapPlayersToPlayerModels(players,placedMoles,currentPlayerId);

    // Set custom cursor
    var cursor = new Image(Utils.getSprite("game/cursor.png"));
    borderPane.setCursor(new ImageCursor(cursor,
      cursor.getWidth() / 2,
      cursor.getHeight() / 2));

    // Create a game handler and add random players to it
    gameHandler = new GameHandler(playerModelList, BOARD_RADIUS, updateFloor(gameState),borderPane);
    gameHandler.start(playerModelList);

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

  public ArrayList<PlayerModel> mapPlayersToPlayerModels(ArrayList<Player> players,ArrayList<Mole>placedMoles,Integer currentPlayerId)
  {
    ArrayList<PlayerModel> playerModelList = new ArrayList<>();
    for (var player:players) {
      ArrayList<MoleModel> moleModelList = new ArrayList<>();
      for (var mole : placedMoles)
      {
        if(player.getClientID() == mole.getPlayer().getClientID())
        {
          moleModelList.add(new MoleModel(player.getClientID(),mole));
        }
      }
      playerModelList.add(new PlayerModel(player,moleModelList,player.getClientID() == currentPlayerId));
    }
    return playerModelList;
  }


  public void updateGameBoard()
  {
    //TODO: Daten bei Änderung von Map entnehmen x_alt + y_alt zu x_neu + y_neu

    //get gameState
    var gameState = CLIENT.getGameState();
    if (gameState== null) return;

    //get current player
    var currentPlayerId = gameState.getCurrentPlayer()== null  ? -1 : gameState.getCurrentPlayer().getClientID();

    // create list of playerModels for ui
    ArrayList<Player> players = gameState.getActivePlayers();
    ArrayList<Mole> placedMoles = gameState.getPlacedMoles();
    var playerModelList = mapPlayersToPlayerModels(players,placedMoles,currentPlayerId);
    gameHandler.update(playerModelList);

    //Update floor if radius changed
    HashMap<List<Integer>, NodeType> nodes = new HashMap<>();
    if (BOARD_RADIUS != gameState.getRadius()) {
      BOARD_RADIUS = gameState.getRadius();
      nodes = updateFloor(gameState);
    }
  }

  public HashMap<List<Integer>, NodeType> updateFloor(GameState gameState)
  {
    HashMap<List<Integer>, NodeType> nodes = new HashMap<>();
    gameState.getFloor().getHoles().forEach(field -> nodes.put(List.of(field.getX(),field.getY()), NodeType.HOLE));
    gameState.getFloor().getDrawAgainFields().forEach(field -> nodes.put(List.of(field.getX(),field.getY()), NodeType.DRAW_AGAIN));
    return nodes;
  }

}
