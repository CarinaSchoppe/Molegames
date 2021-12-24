/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 24.12.21, 12:26 by Carina Latest changes made by Carina on 24.12.21, 12:20
 * All contents of "MainGUI" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import de.thundergames.playmechanics.tournament.Tournament;
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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Carina, Jana, Eva
 * @use the main gui of the "ausrichter"
 */
@Getter
public class MainGUI extends Application implements Initializable {

  private static MainGUI GUI;
  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private Button breakButton;
  @FXML private Button continueButton;
  @FXML private Button createGame;
  @FXML private Button createTournament;
  @FXML private Button editGame;
  @FXML private Button end;
  @FXML private TableColumn<Game, Integer> gameID;
  @FXML private TableColumn<Game, Integer> gamePlayerCount;
  @FXML private TableColumn<Game, String> gameState;
  @FXML private TableView<Game> gameTable;
  @FXML private Button startGame;
  @FXML private TableColumn<Tournament, Integer> tournamentID;
  @FXML private TableColumn<Tournament, Integer> tournamentPlayerCount;
  @FXML private TableColumn<Tournament, String> tournamentState;
  @FXML private TableView<Tournament> tournamentTable;

  public static void create(@NotNull final Server server, @NotNull final String... args) {
    MoleGames.getMoleGames().setAusrichterClient(new AusrichterClient(server));
    new Thread(() -> launch(args)).start();
    // MoleGames.getMoleGames().getAusrichterClient().testTournament(0);
    // MoleGames.getMoleGames().getAusrichterClient().testGame(0);
  }

  public static MainGUI getGUI() {
    return GUI;
  }

  @FXML
  void onBreak(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      JOptionPane.showMessageDialog(
          null, "Es wurde kein Spiel selektiert!", "Spiel auswählen!", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(
            null, "Spiel wurde erfolgreich pausiert!", "Erfolg!", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(
            null,
            "Das Spiel ist nicht im Started GameState!",
            "Spiel Gamestate!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  @FXML
  void onContinue(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      JOptionPane.showMessageDialog(
          null, "Es wurde kein Spiel selektiert!", "Spiel auswählen!", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(
            null, "Spiel wurde erfolgreich weitergeführt!", "Erfolg!", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(
            null,
            "Das Spiel ist nicht im Paused GameState!",
            "Spiel Gamestate!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  @FXML
  void onEnd(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      JOptionPane.showMessageDialog(
          null, "Es wurde kein Spiel selektiert!", "Spiel auswählen!", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(
            null, "Spiel wurde erfolgreich beendet!", "Erfolg!", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(
            null,
            "Das Spiel ist nicht irgendwie am laufen!",
            "Spiel Gamestate!",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  @FXML
  void onStartGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      JOptionPane.showMessageDialog(
          null, "Es wurde kein Spiel selektiert!", "Spiel auswählen!", JOptionPane.ERROR_MESSAGE);
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
        JOptionPane.showMessageDialog(
            null, "Spiel wurde erfolgreich gestartet!", "Erfolg!", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(null, "Das Spiel ist kann nicht gestartet werden!");
      }
    }
  }

  @FXML
  void onCreateGame(ActionEvent event) throws Exception {
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

  @FXML
  void onCreateTournament(ActionEvent event) {
    // TODO: create and start mechanics on creating a tournament
  }

  @FXML
  void onEditGame(ActionEvent event) throws IOException {
    if (gameTable.getSelectionModel().getSelectedItem() != null) {
      var selectedItem = gameTable.getSelectionModel().getSelectedItem();
      var game =
          MoleGames.getMoleGames().getGameHandler().getIDGames().get(selectedItem.getGameID());
      var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      new PlayerManagement(game).start(primaryStage);
    }
  }

  @FXML
  void onEditTournament(ActionEvent event) {
    // TODO: create and start mechanics on editing a tournament to add and remove players
  }

  public void updateTable() {
    gameTable.getItems().clear();
    tournamentTable.getItems().clear();
    gameTable.getItems().addAll(MoleGames.getMoleGames().getGameHandler().getGames());
    tournamentTable.getItems().addAll(MoleGames.getMoleGames().getGameHandler().getTournaments());
  }

  /**
   * @param primaryStage
   * @throws Exception
   * @author Carina, Eva, Jana
   * @use starts the main GUI
   */
  @Override
  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/Main.fxml"));
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

  @FXML
  void initialize() {
    assert breakButton != null
        : "fx:id=\"breakButton\" was not injected: check your FXML file 'Main.fxml'.";
    assert continueButton != null
        : "fx:id=\"continueButton\" was not injected: check your FXML file 'Main.fxml'.";
    assert createGame != null
        : "fx:id=\"createGame\" was not injected: check your FXML file 'Main.fxml'.";
    assert createTournament != null
        : "fx:id=\"createTournament\" was not injected: check your FXML file 'Main.fxml'.";
    assert editGame != null
        : "fx:id=\"editGame\" was not injected: check your FXML file 'Main.fxml'.";
    assert end != null : "fx:id=\"end\" was not injected: check your FXML file 'Main.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'Main.fxml'.";
    assert gamePlayerCount != null
        : "fx:id=\"gamePlayerCount\" was not injected: check your FXML file 'Main.fxml'.";
    assert gameState != null
        : "fx:id=\"gameState\" was not injected: check your FXML file 'Main.fxml'.";
    assert gameTable != null
        : "fx:id=\"gameTable\" was not injected: check your FXML file 'Main.fxml'.";
    assert startGame != null
        : "fx:id=\"startGame\" was not injected: check your FXML file 'Main.fxml'.";
    assert tournamentID != null
        : "fx:id=\"tournamentID\" was not injected: check your FXML file 'Main.fxml'.";
    assert tournamentPlayerCount != null
        : "fx:id=\"tournamentPlayerCount\" was not injected: check your FXML file 'Main.fxml'.";
    assert tournamentState != null
        : "fx:id=\"tournamentState\" was not injected: check your FXML file 'Main.fxml'.";
    assert tournamentTable != null
        : "fx:id=\"tournamentTable\" was not injected: check your FXML file 'Main.fxml'.";
    gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithGameID"));
    gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    tournamentID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithTournamentID"));
    tournamentPlayerCount.setCellValueFactory(
        new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    tournamentState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    GUI = this;
    initialize();
  }
}
