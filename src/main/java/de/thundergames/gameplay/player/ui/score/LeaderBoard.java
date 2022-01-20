/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:21 by Carina Latest changes made by Carina on 09.01.22, 21:18 All contents of "LeaderBoard" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.score;

import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.playmechanics.util.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LeaderBoard extends Application implements Initializable {

  @FXML
  private TableView<PlayerResult> scoreTable;

  @FXML
  private TableColumn<PlayerResult, Integer> placement;

  @FXML
  private TableColumn<PlayerResult, String> name;

  @FXML
  private TableColumn<PlayerResult, Integer> score;

  private Score gameScore;

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    scoreTable.setSelectionModel(null);
    placement.setCellValueFactory(new PropertyValueFactory<>("placement"));
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    score.setCellValueFactory(new PropertyValueFactory<>("score"));
    if (Client.getClientInstance() == null) return;
    createLeaderboard();
  }

  public void create(Score score) {
    this.gameScore = score;
  }

  /**
   * @author Lennart, Carina
   * @use creates a leaderboard and fills it with the playerScores depending on the placement
   * (points)
   * @see Score
   * @see Player
   */
  void createLeaderboard() {
    // sort players in list by their points
    // fill sorted players with their placement, name and points into leaderList
    var leaderList = new ArrayList<PlayerResult>();
    var thisPlace = 1;
    var highestScore = -1;
    Player highestPlayer = null;
    ArrayList<Player> players = new ArrayList<>();
    for (var p : gameScore.getPlayers()) {
      players.add(p);
    }
    var size = players.size();
    while (leaderList.size() != size) {
      for (var player : players) {
        var playerScore = 0;
        if (gameScore.getPoints().get(player.getClientID()) != null) {
          playerScore = gameScore.getPoints().get(player.getClientID());
        }
        if (highestScore < playerScore) {
          highestScore = playerScore;
          highestPlayer = player;
        }
      }
      var playerName = Integer.toString(highestPlayer.getClientID());
      if (highestPlayer.getName() != null) {
        playerName = playerName + "/" + highestPlayer.getName();
      }
      leaderList.add(
              new PlayerResult(playerName, highestScore, thisPlace));
      players.remove(highestPlayer);
      highestScore = -1;
      highestPlayer = null;
      thisPlace++;
    }
    scoreTable.getItems().addAll(leaderList);
  }

  /**
   * Is called when the close button is clicked. Logout user.
   *
   * @param stage current stage
   */
  private void logout(@NotNull final Stage stage) {
    Client.getClientInstance().getClientPacketHandler().logoutPacket();
    stage.close();
  }

  /**
   * Call scene PlayerMenu
   *
   * @param event event from the current scene to build PlayerMenu on same object
   * @throws IOException error creating the scene PlayerMenu
   */
  @FXML
  void backToMenu(@NotNull final ActionEvent event) throws IOException {
    new PlayerMenu().create(event);
  }

  /**
   * @param primaryStage stage that will be opened
   * @throws IOException error creating the scene LeaderBoard
   * @use Create the Scene for LeaderBoard
   * @author Lennart, Carina
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    var loader = SceneController.loadFXML("/player/style/LeaderBoard.fxml");
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Leader Board");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    // region Create button events
    // set event for backToMenu button
    var btnBack = (Button) (primaryStage.getScene().lookup("#btnToMenu"));
    btnBack.setOnAction(
      e -> {
        try {
          backToMenu(e);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
  }
}
