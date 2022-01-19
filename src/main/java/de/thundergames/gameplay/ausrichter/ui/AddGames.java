package de.thundergames.gameplay.ausrichter.ui;

import java.net.URL;
import java.util.ResourceBundle;

import de.thundergames.playmechanics.game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class AddGames {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button back;

  @FXML
  private Button createGame;

  @FXML
  private Button editGame;

  @FXML
  private TableColumn<Game, Integer> gameID;

  @FXML
  private TableColumn<Game, Integer> gamePlayerCount;

  @FXML
  private TableColumn<Game, String> gameState;

  @FXML
  private TableView<Game> gameTable;

  @FXML
  private Button ready;

  @FXML
  void onBack(ActionEvent event) {

  }

  @FXML
  void onCreateGame(ActionEvent event) throws Exception {
      if (CreateGame.getCreateGameInstance() != null) {
        CreateGame.setPunishmentPrev(null);
        CreateGame.setVisualEffectsPrev(null);
        CreateGame.setThinkTimePrev(null);
        CreateGame.getFloors().clear();
        CreateGame.setPullDiscsOrderedPrev(false);
        CreateGame.setRadiusPrev(null);
        CreateGame.getDrawCardValuesList().clear();
        CreateGame.setMaxPlayersPrev(null);
        CreateGame.setMolesAmountPrev(null);
      }
      var primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      new CreateGame().start(primaryStage);
    }


  @FXML
  void onEditGame(ActionEvent event) {

  }

  @FXML
  void onReady(ActionEvent event) {

  }

  @FXML
  void initialize() {
    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert createGame != null : "fx:id=\"createGame\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert editGame != null : "fx:id=\"editGame\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gameID != null : "fx:id=\"gameID\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gamePlayerCount != null : "fx:id=\"gamePlayerCounter\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gameState != null : "fx:id=\"gameState\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert gameTable != null : "fx:id=\"gameTable\" was not injected: check your FXML file 'AddGames.fxml'.";
    assert ready != null : "fx:id=\"ready\" was not injected: check your FXML file 'AddGames.fxml'.";

  }
  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/AddGames.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Spiele hinzufügen");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
