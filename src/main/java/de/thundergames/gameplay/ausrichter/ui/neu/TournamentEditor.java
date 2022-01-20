package de.thundergames.gameplay.ausrichter.ui.neu;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class TournamentEditor {

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
  private Button endGame;

  @FXML
  private TableColumn<?, ?> gameID;

  @FXML
  private TableColumn<?, ?> gamePlayerCount;

  @FXML
  private TableColumn<?, ?> gameTable;

  @FXML
  private Button score;

  @FXML
  private Button startGame;

  @FXML
  void onAddPlayer(ActionEvent event) {

  }

  @FXML
  void onBack(ActionEvent event) {

  }

  @FXML
  void onBreakGame(ActionEvent event) {

  }

  @FXML
  void onContinueGame(ActionEvent event) {

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
    assert addPlayer != null : "fx:id=\"addPlayer\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert breakGame != null : "fx:id=\"breakGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert continueGame != null : "fx:id=\"continueGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert endGame != null : "fx:id=\"endGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gamePlayerCount != null : "fx:id=\"gamePlayerCount\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert score != null : "fx:id=\"score\" was not injected: check your FXML file 'TournamentEditor.fxml'.";
    assert startGame != null : "fx:id=\"startGame\" was not injected: check your FXML file 'TournamentEditor.fxml'.";

  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/TournamentEditor.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Turnier bearbeiten");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

}
