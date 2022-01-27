package de.thundergames.gameplay.ausrichter.ui;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class GameScores {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button back;

  @FXML
  private TableView<?> gameTable;

  @FXML
  private TableColumn<?, ?> name;

  @FXML
  private TableColumn<?, ?> placement;

  @FXML
  private TableColumn<?, ?> score;

  @FXML
  void onBack(ActionEvent event) {

  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file '1.2.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file '1.2.fxml'.";
    assert name != null : "fx:id=\"name\" was not injected: check your FXML file '1.2.fxml'.";
    assert placement != null : "fx:id=\"placement\" was not injected: check your FXML file '1.2.fxml'.";
    assert score != null : "fx:id=\"score\" was not injected: check your FXML file '1.2.fxml'.";



  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/GameScores.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Punkteübersicht");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }

}
