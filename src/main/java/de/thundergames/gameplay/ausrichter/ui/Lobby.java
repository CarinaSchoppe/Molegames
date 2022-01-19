package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Lobby {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button games;

  @FXML
  private Button tournaments;

  @FXML
  void onGames(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new Games().start(primaryStage);
  }


  @FXML
  void onTournaments(ActionEvent event) throws Exception {
    var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    new Tournaments().start(primaryStage);
  }

  @FXML
  void initialize() {
    assert games != null : "fx:id=\"games\" was not injected: check your FXML file 'Lobby.fxml'.";
    assert tournaments != null : "fx:id=\"tournaments\" was not injected: check your FXML file 'Lobby.fxml'.";

  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/Lobby.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Lobby!");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

}
