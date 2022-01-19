package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.playmechanics.tournament.Tournament;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class Tournaments extends Application implements Initializable {

  private static Tournaments TournamentsInstance;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button back;

  @FXML
  private Button createTournament;

  @FXML
  private Button editTournament;

  @FXML
  private TableColumn<Tournament, Integer> tournamentID;
  @FXML
  private TableColumn<Tournament, Integer> tournamentPlayerCount;
  @FXML
  private TableColumn<Tournament, String> tournamentState;
  @FXML
  private TableView<Tournament> tournamentTable;


  @FXML
  void onBack(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    MoleGames.getMoleGames().getGui().start(primaryStage);
  }
  @FXML

  void onCreateTournament(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new CreateTournaments().start(primaryStage);
  }

  @FXML
  void onEditTournament(ActionEvent event) {

  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'Tournaments.fxml'.";
    assert createTournament != null : "fx:id=\"createTournament\" was not injected: check your FXML file 'Tournaments.fxml'.";
    assert editTournament != null : "fx:id=\"editTournament\" was not injected: check your FXML file 'Tournaments.fxml'.";
    assert tournamentID != null : "fx:id=\"tournamentID\" was not injected: check your FXML file 'Tournaments.fxml'.";
    assert tournamentPlayerCount != null : "fx:id=\"tournamentPlayerCount\" was not injected: check your FXML file 'Tournaments.fxml'.";
    assert tournamentState != null : "fx:id=\"tournamentState\" was not injected: check your FXML file 'Tournaments.fxml'.";
    assert tournamentTable != null : "fx:id=\"tournamentTable\" was not injected: check your FXML file 'Tournaments.fxml'.";

    tournamentID.setCellValueFactory(new PropertyValueFactory<>("HashtagWithTournamentID"));
    tournamentPlayerCount.setCellValueFactory(
      new PropertyValueFactory<>("CurrentPlayerCount_MaxCount"));
    tournamentState.setCellValueFactory(new PropertyValueFactory<>("StatusForTableView"));
    updateTable();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TournamentsInstance = this;
    initialize();
  }

  /**
   * @param primaryStage
   * @throws Exception
   * @author Carina, Eva, Jana
   * @use starts the main GUI
   */

  private void updateTable() {
  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/Tournaments.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Turnierübersicht");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    System.out.println("Ich bins start");
    primaryStage.show();
  }
}
