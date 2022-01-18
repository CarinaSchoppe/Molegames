/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 10.01.22, 22:01 by Carina Latest changes made by Carina on 10.01.22, 22:01 All contents of "AddGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ausrichter.AusrichterClient;
import de.thundergames.networking.server.Server;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.util.Dialog;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Jana
 * @use add a Game into a Tournament
 */
public class AddGame    {

  private static AddGame ADD_GAME;

  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private Button back;

  @FXML private Button breakGame;

  @FXML private Button continueGame;

  @FXML private Button editGame;

  @FXML private Button endGame;

  @FXML private TableColumn<Game, Integer> gameID;

  @FXML private TableColumn<Game, Integer> gamePlayerCount;

  @FXML private TableColumn<Game, String> gameState;

  @FXML private TableView<Game> gameTable;

  @FXML private Button getScore;

  @FXML private Button startGame;

  public static void create(@NotNull final Server server) {
    MoleGames.getMoleGames().setAusrichterClient(new AusrichterClient(server));
    new Thread(Application::launch).start();
    MoleGames.getMoleGames().getAusrichterClient().testGame(0);
  }

  public static AddGame getAddGame() {
    return ADD_GAME;
  }

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    MoleGames.getMoleGames().getGui().start(primaryStage);
  }

  @FXML
  void onBreakGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Es wurde kein Spiel ausgewaehlt!", "Spiel auswaehlen!", Dialog.DialogType.ERROR);
    } else {
      if (MoleGames.getMoleGames()
              .getGameHandler()
              .getIDGames()
              .get(selectedItem.getGameID())
              .getCurrentGameState()
          == GameStates.STARTED) {
        MoleGames.getMoleGames()
            .getGameHandler()
            .getIDGames()
            .get(selectedItem.getGameID())
            .pauseGame();
        Dialog.show("Spiel wurde erfolgreich pausiert!", "Erfolg!", Dialog.DialogType.INFO);
        gameTable.getSelectionModel().clearSelection();
      } else {
        Dialog.show(
            "Das Spiel ist nicht im Started GameState!",
            "Spiel Gamestate!",
            Dialog.DialogType.ERROR);
      }
    }
  }

  @FXML
  void onContinueGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Es wurde kein Spiel ausgewaehlt!", "Spiel auswaehlen!", Dialog.DialogType.ERROR);
    } else {
      if (MoleGames.getMoleGames()
              .getGameHandler()
              .getIDGames()
              .get(selectedItem.getGameID())
              .getCurrentGameState()
          == GameStates.PAUSED) {
        MoleGames.getMoleGames()
            .getGameHandler()
            .getIDGames()
            .get(selectedItem.getGameID())
            .resumeGame();
        gameTable.getSelectionModel().clearSelection();
        Dialog.show("Spiel wurde erfolgreich weitergefuehrt!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show(
            "Das Spiel ist nicht im Paused GameState!",
            "Spiel Gamestate!",
            Dialog.DialogType.ERROR);
      }
    }
  }

  @FXML
  void onEditGame(ActionEvent event) throws IOException {
    if (gameTable.getSelectionModel().getSelectedItem() != null) {
      var selectedItem = gameTable.getSelectionModel().getSelectedItem();
      var game =
          MoleGames.getMoleGames().getGameHandler().getIDGames().get(selectedItem.getGameID());
      var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      gameTable.getSelectionModel().clearSelection();
      new PlayerManagement(game).start(primaryStage);
    } else {
      Dialog.show("Du musst ein Spiel auswählen!", "Fehler", Dialog.DialogType.ERROR);
    }
  }

  @FXML
  void onEndGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Es wurde kein Spiel ausgewaehlt!", "Spiel auswaehlen!", Dialog.DialogType.ERROR);
    } else {
      if (MoleGames.getMoleGames()
                  .getGameHandler()
                  .getIDGames()
                  .get(selectedItem.getGameID())
                  .getCurrentGameState()
              != GameStates.OVER
          && MoleGames.getMoleGames()
                  .getGameHandler()
                  .getIDGames()
                  .get(selectedItem.getGameID())
                  .getCurrentGameState()
              != GameStates.NOT_STARTED) {
        MoleGames.getMoleGames()
            .getGameHandler()
            .getIDGames()
            .get(selectedItem.getGameID())
            .forceGameEnd();
        gameTable.getSelectionModel().clearSelection();
        Dialog.show("Spiel wurde erfolgreich beendet!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show(
            "Das Spiel ist nicht irgendwie am laufen!",
            "Spiel Gamestate!",
            Dialog.DialogType.ERROR);
      }
    }
  }

  @FXML
  void onGetScore(ActionEvent event) {
    // TODO
  }

  @FXML
  void onStartGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show(
          "Es wurde kein Spiel ausgewaehlt!", "Spiel auswaehlen!", Dialog.DialogType.WARNING);
    } else {
      if (MoleGames.getMoleGames()
                  .getGameHandler()
                  .getIDGames()
                  .get(selectedItem.getGameID())
                  .getCurrentGameState()
              == GameStates.NOT_STARTED
          && !MoleGames.getMoleGames()
              .getGameHandler()
              .getIDGames()
              .get(selectedItem.getGameID())
              .getActivePlayers()
              .isEmpty()) {
        MoleGames.getMoleGames()
            .getGameHandler()
            .getIDGames()
            .get(selectedItem.getGameID())
            .startGame(GameStates.STARTED);

        gameTable.getSelectionModel().clearSelection();
        Dialog.show("Spiel wurde erfolgreich gestartet!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show(
            "Das Spiel ist kann nicht gestartet werden!", "Fehler", Dialog.DialogType.ERROR);
      }
    }
  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert breakGame != null
        : "fx:id=\"breakGame\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert continueGame != null
        : "fx:id=\"continueGame\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert editGame != null
        : "fx:id=\"editGame\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert endGame != null
        : "fx:id=\"endGame\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert gameID != null
        : "fx:id=\"gameID\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert gamePlayerCount != null
        : "fx:id=\"gamePlayerCount\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert gameState != null
        : "fx:id=\"gameState\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert gameTable != null
        : "fx:id=\"gameTable\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert getScore != null
        : "fx:id=\"getScore\" was not injected: check your FXML file 'AddGame.fxml'.";
    assert startGame != null
        : "fx:id=\"startGame\" was not injected: check your FXML file 'AddGame.fxml'.";

    gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithGameID"));
    gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();


  }


  public void updateTable() {
    var gameSelection = gameTable.getSelectionModel().getSelectedItem();
    gameTable.getItems().clear();
    gameTable.getItems().addAll(MoleGames.getMoleGames().getGameHandler().getGames());
    gameTable.getSelectionModel().select(gameSelection);
  }


  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/AddGame.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spiel hinzufügen!");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

}
