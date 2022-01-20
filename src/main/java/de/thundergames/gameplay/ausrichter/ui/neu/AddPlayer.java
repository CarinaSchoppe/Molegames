package de.thundergames.gameplay.ausrichter.ui.neu;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class AddPlayer {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button addObserver;

  @FXML
  private Button addPlayer;

  @FXML
  private TableView<?> availablePlayer;

  @FXML
  private Button back;

  @FXML
  private TableColumn<?, ?> playerName;

  @FXML
  private TableColumn<?, ?> playerNameSelection;

  @FXML
  private TableColumn<?, ?> playerNumber;

  @FXML
  private TableColumn<?, ?> playerNumberSelection;

  @FXML
  private TableColumn<?, ?> playerStatus;

  @FXML
  private TableView<?> playerTable;

  @FXML
  private Button removePlayer;

  @FXML
  private Button score;

  @FXML
  void onAddObserver(ActionEvent event) {

  }

  @FXML
  void onAddPlayer(ActionEvent event) {

  }

  @FXML
  void onBack(ActionEvent event) {

  }

  @FXML
  void onRemovePlayer(ActionEvent event) {

  }

  @FXML
  void onScore(ActionEvent event) {

  }

  @FXML
  void initialize() {
    assert addObserver != null : "fx:id=\"addObserver\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert addPlayer != null : "fx:id=\"addPlayer\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert availablePlayer != null : "fx:id=\"availablePlayer\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerName != null : "fx:id=\"playerName\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerNameSelection != null : "fx:id=\"playerNameSelection\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerNumber != null : "fx:id=\"playerNumber\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerNumberSelection != null : "fx:id=\"playerNumberSelection\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerStatus != null : "fx:id=\"playerStatus\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert playerTable != null : "fx:id=\"playerTable\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert removePlayer != null : "fx:id=\"removePlayer\" was not injected: check your FXML file 'AddPlayer.fxml'.";
    assert score != null : "fx:id=\"score\" was not injected: check your FXML file 'AddPlayer.fxml'.";

  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/AddPlayer.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spieler hinzufügen");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}

