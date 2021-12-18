/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 18.12.21, 16:37 by Carina Latest changes made by Carina on 18.12.21, 16:35
 * All contents of "MainGUI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class MainGUI extends Application {

  private URL location;
  @FXML private Button breakGame;

  @FXML private Button continueGame;

  @FXML private Button createGame;

  @FXML private Button createTournament;

  @FXML private Button editGame;

  @FXML private Button editTournament;

  @FXML private Button endGame;

  @FXML private Button startGame;

  @FXML
  void onBreakGame(ActionEvent event) {}

  @FXML
  void onContinueGame(ActionEvent event) {}

  @FXML
  void onCreateGameAction(ActionEvent event) {}

  @FXML
  void onCreateTournament(ActionEvent event) {}

  @FXML
  void onEditGame(ActionEvent event) {}

  @FXML
  void onEditTournament(ActionEvent event) {}

  @FXML
  void onEndGame(ActionEvent event) {}

  @FXML
  void onStartGame(ActionEvent event) {}

  @FXML
  void initialize() {
    assert createGame != null
        : "fx:id=\"createGame\" was not injected: check your FXML file 'MainGUI.fxml'.";
    assert editGame != null
        : "fx:id=\"editGame\" was not injected: check your FXML file 'MainGUI.fxml'.";
    assert startGame != null
        : "fx:id=\"startGame\" was not injected: check your FXML file 'MainGUI.fxml'.";
  }

  /**
   * @param primaryStage
   * @throws Exception
   * @author Lennart
   * @use starts the main GUI
   */
  @Override
  public void start(@NotNull final Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/ausrichter/style/MainGUI.fxml"));
    primaryStage.setResizable(false);
    primaryStage.setTitle("MainGUI");
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
