/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 13.01.22, 22:39 by Carina Latest changes made by Carina on 13.01.22, 22:39 All contents of "AddGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
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
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.tournament.Tournament;
import de.thundergames.playmechanics.util.Dialog;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Getter
/**
 * @author Eva, Jana
 * @use add games to a chosen tournament
 */

public class AddGames implements Initializable{

  private static AddGames AddGamesInstance;

  private String Spielmodus;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button back;

  @FXML
  private Button createGame;

  @FXML
  private Button addGame;

  @FXML
  private TableColumn<Game, Integer> gameID;

  @FXML
  private TableColumn<Game, Integer> gamePlayerCount;

  @FXML
  private TableColumn<Game, String> gameState;

  @FXML
  private TableView<Game> gameTable;

  private Tournament tournament;

  public AddGames(Tournament tournament) {
    this.tournament = tournament;
  }

  public static AddGames getAddGamesInstance() {
    return AddGamesInstance;
  }

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    if(Spielmodus.equalsIgnoreCase("TurnierModus")){
      TournamentEditor.getTournamentEditorInstance().start(primaryStage);
    }else {
      Games.getGamesInstance().start(primaryStage);
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
      new CreateGame().start(primaryStage, Spielmodus);
    }

  @FXML
  void onAddGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Du musst ein Spiel auswählen!", "Achtung!",  Dialog.DialogType.WARNING);
    } else {
      if (MoleGames.getMoleGames()
        .getGameHandler()
        .getIDGames()
        .get(selectedItem.getGameID())
        .getCurrentGameState()
        == GameStates.NOT_STARTED) {
        tournament.addGame(selectedItem);
        Dialog.show("Das Spiel wurde hinzugefügt!", "Erfolg!",  Dialog.DialogType.WARNING);
        updateTable();
      } else {
        if (MoleGames.getMoleGames()
          .getGameHandler()
          .getIDGames()
          .get(selectedItem.getGameID())
          .getCurrentGameState()
          == GameStates.OVER) {
          Dialog.show("Das Spiel ist bereits vorbei", "Achtung!",  Dialog.DialogType.WARNING);
          updateTable();
        }
      }
    }
  }

  public void updateTable() {
    var gameSelection = gameTable.getSelectionModel().getSelectedItem();
    gameTable.getItems().clear();
    gameTable.getItems().addAll(MoleGames.getMoleGames().getGameHandler().getGames());
    gameTable.getSelectionModel().select(gameSelection);
  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert createGame != null : "fx:id=\"createGame\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert addGame != null : "fx:id=\"addGame\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gamePlayerCount != null : "fx:id=\"gamePlayerCounter\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gameState != null : "fx:id=\"gameState\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file 'AddGames.fxml'.";

    gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithGameID"));
    gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    AddGamesInstance = this;
    initialize();
  }

  public void start(@NotNull final Stage primaryStage, String modus) throws Exception {
    Spielmodus = modus;
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/AddGames.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spiele hinzufügen");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
