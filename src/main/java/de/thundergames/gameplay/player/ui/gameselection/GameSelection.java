/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:45 by Carina Latest changes made by Carina on 09.01.22, 21:43 All contents of "GameSelection" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.gameselection;

import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.board.GameBoard;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.gameplay.player.ui.score.LeaderBoard;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.util.Dialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameSelection implements Initializable {

  private static Client CLIENT;
  private static GameSelection GAME_SELECTION;
  @FXML
  private Text playerName;
  @FXML
  private TableView<Game> gameTable;
  @FXML
  private TableColumn<Game, Integer> gameID;
  @FXML
  private TableColumn<Game, String> gamePlayerCount;
  @FXML
  private TableColumn<Game, String> gameState;
  private Stage primaryStage;

  public static GameSelection getGameSelection() {
    return GAME_SELECTION;
  }

  /**
   * @param event event from the current scene to build this scene on same object
   * @throws IOException error creating the scene GameSelection
   * @author Marc
   * @use Create the Scene for GameSelection
   */
  public void create(@NotNull ActionEvent event) throws IOException {
    primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set scene
    var loader = SceneController.loadFXML("/player/style/GameSelection.fxml");
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spiele Auswahl");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    updateTable();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    // region Create button events
    // set event for back button
    var btnBack = (Button) (primaryStage.getScene().lookup("#backToMenu"));
    btnBack.setOnAction(
      e -> {
        try {
          backToMenu(e);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    // set event for spectate game
    var btnSpectateGame = (Button) (primaryStage.getScene().lookup("#spectateGame"));
    btnSpectateGame.setOnAction(
      e -> {
        try {
          spectateGame(e);
        } catch (IOException | InterruptedException ex) {
          ex.printStackTrace();
        }
      });
    // endregion
  }

  /**
   * @param stage current stage
   * @use Is called when the close button is clicked. Logout user.
   * @author Marc
   */
  private void logout(Stage stage) {
    CLIENT.getClientPacketHandler().logoutPacket();
    stage.close();
  }

  /**
   * @param location  of base class Initialize
   * @param resources of base class Initialize
   * @author Marc
   * @use Is called when the object is initialized
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    GAME_SELECTION = this;
    CLIENT = Client.getClientInstance();
    // show username at scene
    playerName.setText("Spieler: " + CLIENT.name);
    // set value for each row
    gameID.setCellValueFactory(new PropertyValueFactory<>("GameID"));
    gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    // load data for tableview
    updateTable();
  }

  /**
   * @author Marc
   * @use Refresh the games of tableview
   */
  public void updateTable() {
    // clear tableview and get games from server and add all to table view
    Platform.runLater(() -> {
      gameTable.getItems().clear();
      gameTable.getItems().addAll(CLIENT.getGames());
      gameTable.getSortOrder().add(gameID);
    });
  }

  /**
   * @param event event from the current scene to build PlayerMenu on same object
   * @throws IOException error creating the scene PlayerMenu
   * @author Marc
   * @use Call scene PlayerMenu
   */
  @FXML
  void backToMenu(ActionEvent event) throws IOException {
    new PlayerMenu().create(event);
  }

  /**
   * @param event event from the current scene to build next scene on same object
   * @throws IOException error at creating the scene
   * @author Marc, Issam, Philipp
   * @use Observe the game. If it is over, show score board, otherwise send join game packet and if it is not started
   * join the spectator lobby.
   */
  @FXML
  void spectateGame(ActionEvent event) throws IOException, InterruptedException {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    // If no item of tableview is selected.
    if (selectedItem == null) {
      Dialog.show("Es wurde kein Spiel ausgewählt!", "Spiel beobachten", Dialog.DialogType.ERROR);
      return;
    }
    else {
      if (Objects.equals(selectedItem.getStatus(), GameStates.OVER.toString())) {
        loadScoreboard(selectedItem.getScore());
      } else {
        // Unregister as an Overview Observer and send Packet to join game to get GameState
        CLIENT.getClientPacketHandler().unregisterOverviewObserverPacket();
        CLIENT.getClientPacketHandler().joinGamePacket(selectedItem.getGameID(), false);
        if (Objects.equals(selectedItem.getStatus(), GameStates.NOT_STARTED.toString())) {
          new LobbyObserverGame().create(primaryStage, selectedItem.getGameID());
        }
      }
    }
  }

  public TableView<Game> getGameTable() {
    return gameTable;
  }

  /**
   * @author Philipp
   * @use Load scoreboard of game that is already over
   */
   public void loadScoreboard(Score score) throws IOException {
    LeaderBoard leaderBoard = new LeaderBoard();
    leaderBoard.create(score);
    leaderBoard.start(primaryStage);
   }

  /**
   * @author Marc, Issam, Philipp
   * Load scene of game
   */
  public void spectateGame() {
    Platform.runLater(() -> {
      var status = CLIENT.getGameState().getStatus();
      if (Objects.equals(status, GameStates.STARTED.toString())
              || Objects.equals(status, GameStates.PAUSED.toString())) {
        new GameBoard().create(primaryStage,false);
      }
    });
  }

  /**
   * @author Philipp
   * Join an assigned game
   */
  public void joinAssignedGame(){
    Platform.runLater(() -> {
      CLIENT.getClientPacketHandler().unregisterOverviewObserverPacket();
      CLIENT.getClientPacketHandler().joinGamePacket(CLIENT.getGameID(), false);
      var status = CLIENT.getGameState().getStatus();
      if (Objects.equals(status, GameStates.NOT_STARTED.toString())) {
        try {
          new LobbyObserverGame().create(primaryStage, CLIENT.getGameID());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      else if (Objects.equals(status, GameStates.STARTED.toString())
              || Objects.equals(status, GameStates.PAUSED.toString())) {
        new GameBoard().create(primaryStage, false);
      }
    });
  }


}
