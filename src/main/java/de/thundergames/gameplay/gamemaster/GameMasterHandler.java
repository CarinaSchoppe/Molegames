package de.thundergames.gameplay.gamemaster;

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
import java.util.ResourceBundle;

public class GameMasterHandler extends Application {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button StartGame;

  @FXML
  private Button ViewGame;

  @FXML
  private Button startButton;

  @FXML
  void OpenSomeThing(ActionEvent event) {
  }

  @FXML
  void initialize() {
    assert StartGame != null : "fx:id=\"StartGame\" was not injected: check your FXML file 'GameMasterHandler.fxml'.";
    assert ViewGame != null : "fx:id=\"ViewGame\" was not injected: check your FXML file 'GameMasterHandler.fxml'.";
    assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'GameMasterHandler.fxml'.";
  }
    @Override
  public void start(Stage primaryStage) throws Exception {
      location = new File("src/main/resources/GameMasterHandler.fxml").toURI().toURL();
      Parent root = FXMLLoader.load(location);
      primaryStage.setTitle("GameMasterHandler");
      primaryStage.setScene(new Scene(root, 900, 600));
      primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
