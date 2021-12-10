package de.thundergames.gameplay.ausrichter.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class MainGUI extends Application {

  private URL location;
  @FXML
  private Button breakGame;

  @FXML
  private Button continueGame;

  @FXML
  private Button createGame;

  @FXML
  private Button createTournament;

  @FXML
  private Button editGame;

  @FXML
  private Button editTournament;

  @FXML
  private Button endGame;

  @FXML
  private Button startGame;

  @FXML
  void onBreakGame(ActionEvent event) {
  }

  @FXML
  void onContinueGame(ActionEvent event) {
  }

  @FXML
  void onCreateGameAction(ActionEvent event) {
  }

  @FXML
  void onCreateTournament(ActionEvent event) {
  }

  @FXML
  void onEditGame(ActionEvent event) {
  }

  @FXML
  void onEditTournament(ActionEvent event) {
  }

  @FXML
  void onEndGame(ActionEvent event) {
  }

  @FXML
  void onStartGame(ActionEvent event) {
  }

  @FXML
  void initialize() {
    assert createGame != null : "fx:id=\"createGame\" was not injected: check your FXML file 'MainGUI.fxml'.";
    assert editGame != null : "fx:id=\"editGame\" was not injected: check your FXML file 'MainGUI.fxml'.";
    assert startGame != null : "fx:id=\"startGame\" was not injected: check your FXML file 'MainGUI.fxml'.";
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    location =
      new File("src/main/resources/ausrichter/style/MainGUI.fxml")
        .toURI()
        .toURL();
    Parent root = FXMLLoader.load(location);
    primaryStage.setResizable(false);
    primaryStage.setTitle("MainGUI");
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
