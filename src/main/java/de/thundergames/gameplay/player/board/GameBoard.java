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
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.util.Mole;
import de.thundergames.playmechanics.util.Player;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

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

  private GameState gameState;

  private ArrayList<Player> players;

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
    gameState = CLIENT.getGameState();
    if (gameState== null) return;

    // get radius
    BOARD_RADIUS = gameState.getRadius();

    //get current player
    var currentPlayerId = gameState.getCurrentPlayer()== null  ? -1 : gameState.getCurrentPlayer().getClientID();

    // create list of playerModels for ui
   players = gameState.getActivePlayers();
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
    var currentPlayerId = CLIENT.getCurrentPlayer()== null  ? -1 : CLIENT.getCurrentPlayer().getClientID();

    //get moles
    var fieldMap = CLIENT.getMap().getFieldMap();
    ArrayList<Mole> placedMoles = new ArrayList<>();
    for (var field :fieldMap.values())
    {
      var currentMole = field.getMole();
      if (currentMole != null) {
        if (currentMole.getField().getX()!=field.getX() || currentMole.getField().getY()!=field.getY())
        {
          currentMole.setField(field);
          System.out.println(currentMole.getField().getX() + " " + currentMole.getField().getY() + "/ " + field.getX() + " " +  field.getY());
        }
        placedMoles.add(currentMole);
      }
    }

    var playerModelList = mapPlayersToPlayerModels(players,placedMoles,currentPlayerId);
    gameHandler.update(playerModelList);
  }

  public HashMap<List<Integer>, NodeType> updateFloor(GameState gameState)
  {
    HashMap<List<Integer>, NodeType> nodes = new HashMap<>();
    gameState.getFloor().getHoles().forEach(field -> nodes.put(List.of(field.getX(),field.getY()), NodeType.HOLE));
    gameState.getFloor().getDrawAgainFields().forEach(field -> nodes.put(List.of(field.getX(),field.getY()), NodeType.DRAW_AGAIN));
    return nodes;
  }
}
