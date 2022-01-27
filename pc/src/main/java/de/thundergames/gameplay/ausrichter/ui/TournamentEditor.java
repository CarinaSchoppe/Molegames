/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 20.01.22, 17:01 by Carina Latest changes made by Carina on 20.01.22, 17:00 All contents of "TournamentEditor" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;

import de.thundergames.MoleGames;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameState;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.tournament.Tournament;
import de.thundergames.playmechanics.util.Dialog;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class TournamentEditor implements Initializable {

  private static TournamentEditor TournamentEditorInstance;
  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button addGame;

  @FXML
  private Button managePlayer;

  @FXML
  private Button removeGame;

  @FXML
  private Button back;

  @FXML
  private Button breakGame;

  @FXML
  private Button continueGame;

  @FXML
  private Button endGame;

  @FXML
  private TableColumn<Game, Integer> gameID;

  @FXML
  private TableColumn<Game, Integer> gamePlayerCount;

  @FXML
  private TableColumn<Game, String> gameState;

  @FXML
  private TableView<Game> gameTable;

  @FXML
  private Button score;

  @FXML
  private Button startGame;

  private Tournament tournament;

  public TournamentEditor(Tournament tournament) {
    this.tournament=tournament;
  }

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Tournaments.getTournamentsInstance().start(primaryStage);
  }

  @FXML
  void onScore(ActionEvent event) {

  }

  @FXML
  void onAddGame(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new AddGames(tournament).start(primaryStage, "TurnierModus");
  }

  @FXML
  void onRemoveGame(ActionEvent event) {
    if (gameTable.getSelectionModel().getSelectedItem() != null) {
      var selectedItem = gameTable.getSelectionModel().getSelectedItem();
      tournament.getGames().remove(selectedItem);
      updateTable();
    }
  }

  @FXML
  void onManagePlayer(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    if (gameTable.getSelectionModel().getSelectedItem() != null) {
      var selectedItem = gameTable.getSelectionModel().getSelectedItem();
      //var game = MoleGames.getMoleGames().getGameHandler().getIDGames().get(selectedItem.getGameID());
      gameTable.getSelectionModel().clearSelection();
      //new AddPlayer(selectedItem).start(primaryStage, "TurnierModus");
      new PlayerManagement(selectedItem).start(primaryStage, "Turniermodus");
    } else {
      Dialog.show("Du musst ein Spiel auswählen!", "Fehler", Dialog.DialogType.ERROR);
    }
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
        updateTable();
        Dialog.show("Spiel wurde erfolgreich pausiert!", "Erfolg!", Dialog.DialogType.INFO);
        gameTable.getSelectionModel().clearSelection();
      } else {
        Dialog.show("Das Spiel ist nicht im Started GameState!", "Spiel Gamestate!", Dialog.DialogType.ERROR);
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
        updateTable();
        Dialog.show("Spiel wurde erfolgreich weitergefuehrt!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show("Das Spiel ist nicht im Paused GameState!", "Spiel Gamestate!", Dialog.DialogType.ERROR);
      }
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
        updateTable();
        Dialog.show("Spiel wurde erfolgreich beendet!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show("Das Spiel ist nicht irgendwie am laufen!", "Spiel Gamestate!", Dialog.DialogType.ERROR);
      }
    }
  }

  @FXML
  void onStartGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    var games = gameTable.getItems();
    for (Game i: games){
      if(i.getCurrentGameState() == GameStates.STARTED || i.getCurrentGameState() == GameStates.PAUSED){
        Dialog.show("Warte bitte noch einen Moment. Nur ein Spiel zur gleichen Zeit!", "Spiel läuft noch!", Dialog.DialogType.WARNING);
        return;
      }
    }
    if (selectedItem == null) {
      Dialog.show("Es wurde kein Spiel ausgewaehlt!", "Spiel auswaehlen!", Dialog.DialogType.WARNING);
    } else {
      if (MoleGames.getMoleGames()
              .getGameHandler()
              .getIDGames()
              .get(selectedItem.getGameID())
              .getCurrentGameState()
              == GameStates.NOT_STARTED
              && selectedItem.getActivePlayers().size() >= 2) {
        MoleGames.getMoleGames()
                .getGameHandler()
                .getIDGames()
                .get(selectedItem.getGameID())
                .startGame(GameStates.STARTED);
        gameTable.getSelectionModel().clearSelection();
        updateTable();
        Dialog.show("Spiel wurde erfolgreich gestartet!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show("Das Spiel ist kann nicht gestartet werden!", "Fehler", Dialog.DialogType.ERROR);
      }
    }
  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert addGame != null : "fx:id=\"addGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert breakGame != null : "fx:id=\"breakGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert continueGame != null : "fx:id=\"continueGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert endGame != null : "fx:id=\"endGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gamePlayerCount != null : "fx:id=\"gamePlayerCount\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gameState != null : "fx:id=\"gameState\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert score != null : "fx:id=\"score\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert startGame != null : "fx:id=\"startGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert managePlayer != null : "fx:id=\"managePlayer\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert removeGame != null : "fx:id=\"removeGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithGameID"));
    gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TournamentEditorInstance=this;
    initialize();
  }

  public static TournamentEditor getTournamentEditorInstance() {
    return TournamentEditorInstance;
  }

  public void updateTable() {
    var gameSelection = gameTable.getSelectionModel().getSelectedItem();
    gameTable.getItems().clear();
    gameTable.getItems().addAll(tournament.getGames());
    gameTable.getSelectionModel().select(gameSelection);
  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/TournamentEditor.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Turnier bearbeiten!");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
