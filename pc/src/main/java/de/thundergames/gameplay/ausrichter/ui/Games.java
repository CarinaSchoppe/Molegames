package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;

import de.thundergames.MoleGames;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.game.GameStates;
import de.thundergames.playmechanics.tournament.Tournament;
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
/**
 * @author Eva, Jana
 * @use to manage create games
 */
public class Games extends Application implements Initializable {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button addPlayer;

  @FXML
  private Button back;

  @FXML
  private Button breakGame;

  @FXML
  private Button continueGame;

  @FXML
  private Button createGame;

  @FXML
  private Button endGame;

  @FXML private TableColumn<Game, Integer> gameID;

  @FXML private TableColumn<Game, Integer> gamePlayerCount;

  @FXML private TableColumn<Game, String> gameState;

  @FXML private TableView<Game> gameTable;

  @FXML
  private Button score;

  @FXML
  private Button startGame;

  private static Games GamesInstance;

  public static Games getGamesInstance() {
    return GamesInstance;
  }

  @FXML
  void onCreateGame(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new CreateGame().start(primaryStage, "SpielModus");
  }

  @FXML
  void onAddPlayer(ActionEvent event) throws Exception {
    if (gameTable.getSelectionModel().getSelectedItem() != null) {
      var selectedItem = gameTable.getSelectionModel().getSelectedItem();
      var game =
              MoleGames.getMoleGames().getGameHandler().getIDGames().get(selectedItem.getGameID());
      var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      gameTable.getSelectionModel().clearSelection();
      //new AddPlayer(game).start(primaryStage, "Spielmodus");
      new PlayerManagement(game).start(primaryStage, "Spielmodus");
    } else {
      Dialog.show("Du musst ein Spiel auswählen!", "Achtung!", Dialog.DialogType.WARNING);
    }
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
      Dialog.show("Du musst ein Spiel auswählen!", "Achtung!", Dialog.DialogType.WARNING);
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
        Dialog.show("Das gewählte Spiel wurde erfolgreich pausiert!", "Erfolg!", Dialog.DialogType.INFO);
        gameTable.getSelectionModel().clearSelection();
      } else {
        Dialog.show("Das gewählte Spiel wurde noch nicht gestartet!", "Achtung!", Dialog.DialogType.WARNING);
      }
    }
  }

  @FXML
  void onContinueGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Du musst ein Spiel auswählen!", "Achtung!", Dialog.DialogType.WARNING);
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
        Dialog.show("Das gewählte Spiel geht weiter!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show("Das gewählte Spiel ist nicht pausiert!", "Achtung!", Dialog.DialogType.WARNING);
      }
    }
  }

  @FXML
  void onEndGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Du musst ein Spiel auswählen!", "Achtung!", Dialog.DialogType.WARNING);
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
        Dialog.show("Das gewählte Spiel wurde erfolgreich beendet!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show("Das gewählte Spiel kann nicht beendet werden!", "Achtung!", Dialog.DialogType.WARNING);
      }
    }
  }

  @FXML
  void onScore(ActionEvent event) {

  }

  @FXML
  void onStartGame(ActionEvent event) {
    var selectedItem = gameTable.getSelectionModel().getSelectedItem();
    if (selectedItem == null) {
      Dialog.show("Du musst ein Spiel auswählen!", "Achtung!", Dialog.DialogType.WARNING);
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
        Dialog.show("Das gewählte Spiel wurde erfolgreich gestartet!", "Erfolg!", Dialog.DialogType.INFO);
      } else {
        Dialog.show("Status 'Open', dann musst du mindestens 2 Spieler hinzufügen!", "Achtung!", Dialog.DialogType.WARNING);
      }
    }
  }

  @FXML
  void initialize() {
    assert addPlayer != null : "fx:id=\"addPlayer\" was not injected: check your FXML file '1.1.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file '1.1.fxml'.";
    assert breakGame != null : "fx:id=\"breakGame\" was not injected: check your FXML file '1.1.fxml'.";
    assert continueGame != null : "fx:id=\"continueGame\" was not injected: check your FXML file '1.1.fxml'.";
    assert createGame != null : "fx:id=\"createGame\" was not injected: check your FXML file '1.1.fxml'.";
    assert endGame != null : "fx:id=\"endGame\" was not injected: check your FXML file '1.1.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file '1.1.fxml'.";
    assert score != null : "fx:id=\"score\" was not injected: check your FXML file '1.1.fxml'.";
    assert startGame != null : "fx:id=\"startGame\" was not injected: check your FXML file '1.1.fxml'.";

    gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithGameID"));
    gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    GamesInstance = this;
    initialize();
  }

  public void updateTable() {
    var gameSelection = gameTable.getSelectionModel().getSelectedItem();
    gameTable.getItems().clear();
    gameTable.getItems().addAll(MoleGames.getMoleGames().getGameHandler().getGames());
    gameTable.getSelectionModel().select(gameSelection);
  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/Games.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spieleübersicht");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

}
