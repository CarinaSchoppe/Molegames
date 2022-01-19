/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 17.01.22, 19:10 by Carina Latest changes made by Carina on 17.01.22, 19:10 All contents of "TournamentEditor_ALT" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.MoleGames;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @param event
 * @author Eva
 * @throws Exception
 * @use opens tournament mode to create games in it
 */

public class TournamentEditor_ALT extends Application implements Initializable {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button addGAme;

  @FXML
  private Button back;

  @FXML
  private Button deleteGame;

  @FXML
  private TableColumn<?, ?> gameID;

  @FXML
  private TableColumn<?, ?> player;

  @FXML
  private Button releaseTournament;

  @FXML
  private TableView<?> tableTournamentGame;

  /**
   * @param event
   * @throws Exception
   * @author Eva
   * @use open CreateGames to create games in tournament
   */

  @FXML
  void onAddGame(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new AddGame_ALT().start(primaryStage);
  }

  /**
  @FXML
  void onAddGame(ActionEvent event) throws Exception {
    if (CreateGame.getCreateGameInstance() != null) {
      CreateGame.setPunishmentPrev(null);
      CreateGame.setVisualEffectsPrev(null);
      CreateGame.setThinkTimePrev(null);
      CreateGame.getFloors().clear();
      CreateGame.setPullDiscsOrderedPrev(false);
      CreateGame.setRadiusPrev(null);
      CreateGame.getDrawCardValuesList().clear();
      CreateGame.setMaxPlayersPrev(null);
      CreateGame.setMolesAmountPrev(null);
    }
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new CreateGame().start(primaryStage);
  }
   */
  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    /*
    punishmentPrev = null;
    maxPlayersPrev = null;
    molesAmountPrev = null;
    thinkTimePrev = null;
    visualEffectsPrev = null;
    radiusPrev = null;
    drawCardValuesList.clear();
    floors.clear();
    savePrevSettings();
    */
    MoleGames.getMoleGames().getGui().start(primaryStage);
    MoleGames.getMoleGames().getGui().updateTable();
  }

  @FXML
  void onDeleteGame(ActionEvent event) {
  }

  @FXML
  void onGameID(ActionEvent event) {
  }

  @FXML
  void onPlayer(ActionEvent event) {
  }

  @FXML
  void onReleaseTournament(ActionEvent event) {
  }

  @FXML
  void onTableTournamentGame(ActionEvent event) {
  }

  @FXML
  void initialize() {
    assert addGAme != null : "fx:id=\"addGAme\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
    assert deleteGame != null : "fx:id=\"deleteGame\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
    assert player != null : "fx:id=\"player\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
    assert releaseTournament != null : "fx:id=\"releaseTournament\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
    assert tableTournamentGame != null : "fx:id=\"tableTournamentGame\" was not injected: check your FXML file 'Tournament-Editor.fxml'.";
  }

  /**
   * @param primaryStage
   * @throws Exception
   * @author Carina, Eva, Jana
   * @use starts the main GUI
   */

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initialize();
  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/Tournament-Editor.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
