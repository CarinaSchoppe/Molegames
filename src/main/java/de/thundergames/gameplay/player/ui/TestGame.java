package de.thundergames.gameplay.player.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class TestGame extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    var loader = new FXMLLoader();
    try {
      var manager = new ViewManager();
      primaryStage = manager.getMainStage();
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
