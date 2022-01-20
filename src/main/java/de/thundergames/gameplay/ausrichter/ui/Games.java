package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;

import de.thundergames.MoleGames;
import de.thundergames.playmechanics.game.Game;
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
import org.jetbrains.annotations.NotNull;

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
  void onAddPlayer(ActionEvent event) {

  }

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    MoleGames.getMoleGames().getGui().start(primaryStage);
  }

  @FXML
  void onBreakGame(ActionEvent event) {

  }

  @FXML
  void onContinueGame(ActionEvent event) {

  }

  @FXML
  void onCreateGame(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new CreateGame().start(primaryStage, "SpielModus");
  }

  @FXML
  void onEndGame(ActionEvent event) {

  }

  @FXML
  void onScore(ActionEvent event) {

  }

  @FXML
  void onStartGame(ActionEvent event) {

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
