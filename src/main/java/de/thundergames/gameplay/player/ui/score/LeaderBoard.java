/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 08.01.22, 10:59 by Carina Latest changes made by Carina on 08.01.22, 10:52 All contents of "LeaderBoard" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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

  /**
   * @author Carina, Lennart
   * @use launches the Scene
   */
  public void create(@NotNull final Score score) {
    Client.getClientInstance().getGameState().setScore(score);
    try {
      launch();
    } catch (Exception ignored) {
    }
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    scoreTable.setSelectionModel(null);
    placement.setCellValueFactory(new PropertyValueFactory<>("placement"));
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    score.setCellValueFactory(new PropertyValueFactory<>("score"));
    if (Client.getClientInstance() == null) return;
    createLeaderboard();
  }

  /**
   * @author Lennart, Carina
   * @use creates a leaderboard and fills it with the playerScores depending on the placement
   * (points)
   * @see Score
   * @see Player
   */
  void createLeaderboard() {
    var score = Client.getClientInstance().getGameState().getScore();
    // sort players in list by their points
    // fill sorted players with their placement, name and points into leaderlist
    var leaderlist = new ArrayList<PlayerResult>();
    var thisPlace = 1;
    for (var player : score.getPlayers()) {
      leaderlist.add(
        new PlayerResult(
          player.getName(), score.getPoints().get(player.getClientID()), thisPlace));
      thisPlace++;
    }
    scoreTable.getItems().addAll(leaderlist);
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
  public void start(Stage primaryStage) throws Exception {
    var loader = SceneController.loadFXML("/player/style/LeaderBoard.fxml");
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
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
