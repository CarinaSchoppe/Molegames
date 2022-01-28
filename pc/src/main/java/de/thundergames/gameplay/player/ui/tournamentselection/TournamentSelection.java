/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 11.01.22, 20:01 by Carina Latest changes made by Carina on 11.01.22, 19:42 All contents of "TournamentSelection" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.tournamentselection;

import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.board.GameBoard;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.gameplay.player.ui.score.LeaderBoard;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.tournament.Tournament;
import de.thundergames.playmechanics.tournament.TournamentStatus;
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

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TournamentSelection implements Initializable {

  private static Client CLIENT;
  private static TournamentSelection TOURNAMENT_SELECTION;
  @FXML
  private Text PlayerName;
  @FXML
  private TableView<Tournament> gameTable;
  @FXML
  private TableColumn<Tournament, Integer> tournamentID;
  @FXML
  private TableColumn<Tournament, String> playerCount;
  private Stage primaryStage;

  public static TournamentSelection getTournamentSelection() {
    return TOURNAMENT_SELECTION;
  }

  /**
   * Create the Scene for TournamentSelection
   *
   * @param event event from the current scene to build this scene on same object
   * @throws IOException error creating the scene TournamentSelection
   */
  public void create(ActionEvent event) throws IOException {
    primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set scene
    var loader = SceneController.loadFXML("/player/style/TournamentSelection.fxml");
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Turnier Auswahl");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
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
    // set event for spectate game of tournament
    var btnSpectateGame = (Button) (primaryStage.getScene().lookup("#spectateGame"));
    btnSpectateGame.setOnAction(
      e -> {
        try {
          spectateGame(e);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    // endregion
  }

  /**
   * @author Marc
   * Is called when the object is initialized
   * @param location  of base class Initialize
   * @param resources of base class Initialize
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TOURNAMENT_SELECTION = this;
    CLIENT = Client.getClientInstance();
    // show username at scene
    PlayerName.setText("Spieler: " + CLIENT.name);
    // set value for each row
    tournamentID.setCellValueFactory(new PropertyValueFactory<>("TournamentID"));
    playerCount.setCellValueFactory(new PropertyValueFactory<>("playerCount"));
    // load data for tableview
    updateTable();
  }

  /**
   * @author Marc
   * Refresh the games of tableview
   */
  public void updateTable() {
    // clear tableview and get tournaments from server and add all to table view
    gameTable.getItems().clear();
    gameTable.getItems().addAll(CLIENT.getTournaments());
    gameTable.getSortOrder().add(tournamentID);
  }

  /**
   * Button at Scene TournamentSelection. Call scene PlayerMenu
   * @author Marc
   * @param event event from the current scene to build PlayerMenu on same object
   * @throws IOException error creating the scene PlayerMenu
   */
  @FXML
  void backToMenu(ActionEvent event) throws IOException {
    new PlayerMenu().create(event);
    TOURNAMENT_SELECTION = null;
  }

  /**
   * Is called when the close button is clicked. Logout user.
   * @author Marc
   * @param stage current stage
   */
  private void logout(Stage stage) {
    CLIENT.getClientPacketHandler().logoutPacket();
    stage.close();
  }

  /**
   * Button at Scene TournamentSelection. Observe the tournament. If tournament is already started,
   * spectate the tournament, else join the spectator lobby.
   * @author Marc
   * @param event event from the current scene to build next scene on same object
   */
  @FXML
  public void spectateGame(ActionEvent event) throws IOException {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    // If no item of tableview is selected.
    if (selectedItem == null) {
      Dialog.show("Es wurde kein Turnier ausgewaehlt!", "Turnier beobachten!", Dialog.DialogType.ERROR);
      return;
    }
    else {
      if (Objects.equals(selectedItem.getStatus(), TournamentStatus.OVER.toString())) {
        loadScoreboard(selectedItem.getScore());
      } else {
        // Send Packet to join game to get GameState
        CLIENT.getClientPacketHandler().enterTournamentPacket(selectedItem.getTournamentID(), false);
        if (Objects.equals(selectedItem.getStatus(), TournamentStatus.NOT_STARTED.toString())) {
          var lobby = new LobbyObserverTournament();
          lobby.create(primaryStage);
          lobby.setSelectedTournamentID(selectedItem.getTournamentID());
        }
      }
    }
  }

  /**
   * @author Marc
   * @use Load scene of scoreboard
   */
  public void loadScoreboard(Score score) throws IOException {
    LeaderBoard leaderBoard = new LeaderBoard();
    leaderBoard.create(score);
    leaderBoard.start(primaryStage);
  }

  /**
   * @author Marc
   * @use Load scene of game
   */
  public void spectateGame() {
    Platform.runLater(() -> {
      var status = CLIENT.getGameState().getStatus();
      if (Objects.equals(status, GameStates.STARTED.toString())
        || Objects.equals(status, GameStates.PAUSED.toString()))
      {
        var selectedItem = gameTable.getSelectionModel().getSelectedItem();
        GameBoard board = new GameBoard();
        board.create(primaryStage,true);
        board.setTournamentId(selectedItem.getTournamentID());
      }
    });
  }
}
