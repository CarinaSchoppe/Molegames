/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 19:39 by Carina latest changes made by Carina on 06.12.21, 19:39
 * All contents of "PlayerMenu" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui;

import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.tournamentselection.TournamentSelection;
import de.thundergames.gameplay.player.ui.gameselection.GameSelection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerMenu  implements Initializable {


    private static Client client;
    @FXML
    private Text PlayerName;

  /**
   * Create the Scene for PlayerMenu
   * @param event event from the current scene to build this scene on same object
   * @throws IOException error creating the scene PlayerMenu
   */
    public void create(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File("src/main/resources/player/PlayerMenu.fxml")
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setTitle("Maulwurf Company");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    }

  /**
   * Is called when the object is initialized
   * @param location of base class Initialize
   * @param resources of base class Initialize
   */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set client
        client = Client.getClient();
        //show username at scene
        PlayerName.setText("Spieler: " + client.name);
    }

  /**
   * Button at Scene PlayerMenu. Call start scene LoginScreen. Logout the user.
   * @param event event from the current scene to build start scene on same object
   * @throws IOException error creating the scene LoginScreen
   */
    @FXML
    void onSignOutClick(ActionEvent event) throws Exception {
      //logout for user
      client.getClientPacketHandler().logoutPacket(client);
      //create LoginScreen scene
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File("src/main/resources/player/LoginScreen.fxml")
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

  /**
   * Button at Scene PlayerMenu. Call next scene for GameSelection
   * @param event event from the current scene to build next scene on same object
   * @throws IOException error creating the scene GameSelection
   */
  @FXML
    public void onGameClick(ActionEvent event) throws IOException {
        new GameSelection().create(event);
    }

  /**
   * Is called when the close button is clicked.
   * Logout user.
   * @param stage current stage
   */
  private void logout(Stage stage) {
    client.getClientPacketHandler().logoutPacket(client);
    stage.close();
  }

  /**
   * Button at Scene PlayerMenu. Call next scene for TournamentSelection
   * @param event event from the current scene to build next scene on same object
   * @throws IOException error creating the scene TournamentSelection
   */
  @FXML
    public void onTournamentClick(ActionEvent event) throws IOException {
        new TournamentSelection().create(event);
    }
}
