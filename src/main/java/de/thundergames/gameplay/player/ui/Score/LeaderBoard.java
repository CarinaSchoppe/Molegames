package de.thundergames.gameplay.player.ui.Score;

import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.networking.util.interfaceItems.NetworkPlayer;
import de.thundergames.gameplay.util.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LeaderBoard implements Initializable {

  private static Client client;

  @FXML
  private TableView<PlayerResult> scoreTable;

  @FXML
  private TableColumn<PlayerResult, Integer> placement;

  @FXML
  private TableColumn<PlayerResult, String> name;

  @FXML
  private TableColumn<PlayerResult, Integer> score;

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    client = Client.getClient();
    placement.setCellValueFactory(new PropertyValueFactory<>("placement"));
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    score.setCellValueFactory(new PropertyValueFactory<>("score"));

    if (client == null) return;
    //var score = client.getGameState().getScore();
    createLeaderbord();
  }

  /**
   * @author Lennart, Carina
   * @use creates a leaderboard and filles it with the playerscores depending on the placement (points)
   * @see Score
   * @see NetworkPlayer
   */
  void createLeaderbord() {
    ArrayList<PlayerResult> test = new ArrayList<PlayerResult>();
    test.add(new PlayerResult("Marc",100,1));
    test.add(new PlayerResult("Max",90,2));

    scoreTable.getItems().addAll(test);
  }
  //  var list = new ArrayList<NetworkPlayer>();
  //  var players = new ArrayList<>(score.getPlayers());
  //  var current = players.get(0);
  ////  int currentPoints = 0;
  ////  var pointsList = new ArrayList<NetworkPlayer>();
  ////  for (var i = 0; i < score.getPlayers().size(); i++) {
  ////    for (var player : players) {
  ////      if (score.getPoints().get(player.getClientID()) > score.getPoints().get(current.getClientID())) {
  ////        current = player;
  ////        currentPoints = score.getPoints().get(player.getClientID();
  ////      }
  ////    }
  ////    players.remove(current);
  ////    list.add(current);
  ////    pointsList.add(currentPoints);
//
  //  }
    //players in list are sorted by their points
    //for (int place = 0; place < list.length(); place++) {
      //var row = place + "         " + list.get(place).getName() + "       " + score.getPoints().get(list.get(place).getClientID()) + "\n";
      //leaderboard.setText(leaderboard.getText() + row);
      //placeCol.getItems().add(place);
      //playerCol.getItems().add(list.get(place).getName());
      //placeCol.getItems().add(score.getPoints().get(list.get(place).getClientID()));
  //  }
  //}

  public void create(@NotNull ActionEvent event) throws Exception {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set scene
    var loader = SceneController.loadFXML("player/LeaderBoard.fxml");
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));

    // region Create button events
    // set event for back button
    var btnBack = (Button) (primaryStage.getScene().lookup("#btnToMenu"));
    btnBack.setOnAction(
      e -> {
        try {
          backToMenu(e);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });
  }

  /**
   * Is called when the close button is clicked. Logout user.
   *
   * @param stage current stage
   */
  private void logout(Stage stage) {
    client.getClientPacketHandler().logoutPacket(client);
    stage.close();
  }

  /**
   * Call scene PlayerMenu
   *
   * @param event event from the current scene to build PlayerMenu on same object
   * @throws IOException error creating the scene PlayerMenu
   */
  @FXML
  void backToMenu(ActionEvent event) throws IOException {
    new PlayerMenu().create(event);
  }

}