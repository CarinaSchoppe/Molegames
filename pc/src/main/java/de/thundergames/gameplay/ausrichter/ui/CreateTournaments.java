package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;

import de.thundergames.MoleGames;
import de.thundergames.playmechanics.game.Game;
import de.thundergames.playmechanics.util.Mole;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


public class CreateTournaments implements Initializable {

  private static CreateTournaments CreateTournamentsInstance;
  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button chooseGame;

  @FXML
  private Button deleteGame;

  @FXML private TableColumn<Game, Integer> gameID;

  @FXML private TableColumn<Game, Integer> gamePlayerCount;

  @FXML private TableColumn<Game, String> gameState;

  @FXML private TableView<Game> gameTable;
  @FXML

  private Button startTournament;

  private Button back;

  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Tournaments.getTournamentsInstance().start(primaryStage);
  }


  @FXML
  void onChooseGame(ActionEvent event) throws Exception  {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    //new AddGames().start(primaryStage, "TurnierModus");
  }

  @FXML
  void onDeleteGame(ActionEvent event) {

  }

  @FXML
  void onStartTournament(ActionEvent event) {

  }

  public static CreateTournaments getCreateTournamentsInstance() {
    return CreateTournamentsInstance;
  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert chooseGame != null : "fx:id=\"chooseGame\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert deleteGame != null : "fx:id=\"deleteGame\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert gamePlayerCount != null : "fx:id=\"gamePlayerCounter\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert gameState != null : "fx:id=\"gameState\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert startTournament != null : "fx:id=\"startTournament\" was not injected: check your FXML file 'CreateTournaments.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file 'CreateTournaments.fxml'.";

    //MoleGames.getMoleGames().getGameHandler().getIDTournaments().get(1).addGame()
    //gameID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithTournamentID"));
    //gamePlayerCount.setCellValueFactory(new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    //gameState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();
  }

  private void updateTable() {
    var gameSelection = gameTable.getSelectionModel().getSelectedItem();
    gameTable.getItems().clear();
    gameTable.getItems().addAll(MoleGames.getMoleGames().getGameHandler().getGames());
    gameTable.getSelectionModel().select(gameSelection);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    CreateTournamentsInstance=this;
    initialize();
  }
  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/CreateTournaments.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    updateTable();
    primaryStage.setTitle("Turnier erstellen!");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}