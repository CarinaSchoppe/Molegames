/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 15.12.21, 19:20 by Carina Latest changes made by Carina on 15.12.21, 19:19 All contents of "PlayerMenu" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui;

import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.gameselection.GameSelection;
import de.thundergames.gameplay.player.ui.tournamentselection.TournamentSelection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerMenu implements Initializable {

  private static Client CLIENT;
  @FXML
  private Text PlayerName;

  /**
   * Create the Scene for PlayerMenu
   *
   * @param event event from the current scene to build this scene on same object
   * @throws IOException error creating the scene PlayerMenu
   */
  public void create(ActionEvent event) throws IOException {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("/player/style/PlayerMenu.fxml"));
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    CLIENT.getClientPacketHandler().getOverviewPacket();
  }

  /**
   * Is called when the object is initialized
   *
   * @param location  of base class Initialize
   * @param resources of base class Initialize
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //set client
    CLIENT = Client.getClientInstance();
    //show username at scene
    PlayerName.setText("Spieler: " + CLIENT.name);
  }

  /**
   * Button at Scene PlayerMenu. Call start scene LoginScreen. Logout the user.
   *
   * @param event event from the current scene to build start scene on same object
   * @throws IOException error creating the scene LoginScreen
   */
  @FXML
  void onSignOutClick(ActionEvent event) throws Exception {
    //logout for user
    CLIENT.getClientPacketHandler().logoutPacket();
    //create LoginScreen scene
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("/player/style/LoginScreen.fxml"));
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }

  /**
   * Button at Scene PlayerMenu. Call next scene for GameSelection
   *
   * @param event event from the current scene to build next scene on same object
   * @throws IOException error creating the scene GameSelection
   */
  @FXML
  public void onGameClick(ActionEvent event) throws IOException {
    //TODO: soll nochmal senden problem: client.getClientThread ist null
    // client.getClientPacketHandler().getOverviewPacket(client);
    new GameSelection().create(event);
  }

  /**
   * Is called when the close button is clicked.
   * Logout user.
   *
   * @param stage current stage
   */
  private void logout(Stage stage) {
    CLIENT.getClientPacketHandler().logoutPacket();
    stage.close();
  }

  /**
   * Button at Scene PlayerMenu. Call next scene for TournamentSelection
   *
   * @param event event from the current scene to build next scene on same object
   * @throws IOException error creating the scene TournamentSelection
   */
  @FXML
  public void onTournamentClick(ActionEvent event) throws IOException {
    CLIENT.getClientPacketHandler().getOverviewPacket();
    new TournamentSelection().create(event);
  }
}
