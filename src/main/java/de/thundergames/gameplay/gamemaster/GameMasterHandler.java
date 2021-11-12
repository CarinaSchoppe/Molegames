package de.thundergames.gameplay.gamemaster;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class GameMasterHandler extends Application {

  @FXML private ResourceBundle resources;

  @FXML private URL location;

  @FXML private Button StartGame;

  @FXML private Button ViewGame;

  @FXML private Button startButton;

  public static void main(String[] args) {
    launch(args);
  }

  @FXML
  void OpenSomeThing(ActionEvent event) {}

  @Override
  public void start(@NotNull final Stage primaryStage) throws Exception {
    location = new File("src/main/resources/gamemaster/GameMasterHandler.fxml").toURI().toURL();
    Parent root = FXMLLoader.load(location);
    primaryStage.setTitle("GameMasterHandler");
    primaryStage.setScene(new Scene(root, 900, 600));
    primaryStage.show();
  }

  public void do2(ActionEvent actionEvent) {
    System.out.println("do5");
  }

  public void do3(ActionEvent actionEvent) {
    System.out.println("do3");
  }
}
