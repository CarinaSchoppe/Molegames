/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 16.12.21, 16:15 by Carina Latest changes made by Carina on 16.12.21, 15:51 All contents of "GameSelection" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.gameselection;

import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.playmechanics.board.TestWindow;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameSelection implements Initializable {

  private static Client CLIENT;
  private static GameSelection GAME_SELECTION;
  @FXML
  private Text PlayerName;
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
   * @param event event from Fthe current scene to build this scene on same object
   * @throws IOException error creating the scene GameSelection
   * @author Marc
   * @use Create the Scene for GameSelection
   */
  public void create(@NotNull ActionEvent event) throws IOException {
    primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set scene
    var loader = SceneController.loadFXML("/player/style/GameSelection.fxml");
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
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
    PlayerName.setText("Spieler: " + CLIENT.name);
    // set value for each row
    gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithGameID"));
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
    gameTable.getItems().clear();
    gameTable.getItems().addAll(CLIENT.getGames());
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
   * @author Marc
   * @use Observe the game. If game is already started, spectate the game, else join the spectator lobby.
   */
  @FXML
  void spectateGame(ActionEvent event) throws IOException, InterruptedException {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    // If no item of tableview is selected.
    if (selectedItem == null) {
      JOptionPane.showMessageDialog(
        null, "Es wurde kein Spiel selektiert!", "Spiel beobachten", JOptionPane.ERROR_MESSAGE);
      return;
    }
    // Send Packet to spectate game to get GameState
    CLIENT.getClientPacketHandler().joinGamePacket(selectedItem.getGameID(), false);
    boolean waiting = true;
    int counter = 0;
    GameState currentGameState = null;
    while (waiting) {
      Thread.sleep(1000);
      counter += 1;
      currentGameState = CLIENT.getGameState();
      if (counter == 5 || currentGameState != null) {
        waiting = false;
      }
    }
    // Get GameState
    //GameState currentGameState = client.getGameState();
    if (Client.getClientInstance().isDebug()) {
      if (currentGameState == null) {
        System.out.println("GameState is null!");
        return;
      }
    }
    if (Objects.equals(currentGameState.getStatus(), GameStates.STARTED.toString())
      || Objects.equals(currentGameState.getStatus(), GameStates.PAUSED.toString())) {
      spectateGame(currentGameState);
    } else if (Objects.equals(currentGameState.getStatus(), GameStates.NOT_STARTED.toString())) {
      new LobbyObserverGame().create(primaryStage);
    } else if (Objects.equals(currentGameState.getStatus(), GameStates.OVER.toString())) {
      loadScoreboard();
    }
  }

  /**
   * Load scene of scoreboard
   */
  private void loadScoreboard() {
    CLIENT.getClientPacketHandler().getScorePacket();
    var gameScore = CLIENT.getGameState().getScore();
    // Todo:Open scene of ScoreBoard with gameScore and check if it is even possible to do so
  }

  /**
   * Load scene of game
   */
  private void spectateGame(GameState gameState) {
    primaryStage.close();
    new TestWindow().start(primaryStage);
  }
}
