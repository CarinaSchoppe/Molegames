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
import java.util.HashMap;
import java.util.HashSet;
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
    scoreTable.setSelectionModel(null);
    client = Client.getClient();
    placement.setCellValueFactory(new PropertyValueFactory<>("placement"));
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    score.setCellValueFactory(new PropertyValueFactory<>("score"));

    if (client == null) return;
    createLeaderbord();
  }

  /**
   * @author Lennart, Carina
   * @use creates a leaderboard and filles it with the playerscores depending on the placement (points)
   * @see Score
   * @see NetworkPlayer
   */
  void createLeaderbord() {

    client.getClientPacketHandler().getScorePacket(client);  //Muss evtl raus.
    Score score = client.getGameState().getScore();

    var list = new ArrayList<NetworkPlayer>();

    var players = new ArrayList<>(score.getPlayers());

    //sort players in list by their points
    for (var i = 0; i < score.getPlayers().size(); i++) {
      var current = players.get(0);
      for (var player : players) {
        if (score.getPoints().get(player.getClientID()) > score.getPoints().get(current.getClientID())) {
          current = player;
        }
      }
      players.remove(current);
      list.add(current);
    }

    //fill sorted players with their placement, name and points into leaderlist
    ArrayList<PlayerResult> leaderlist = new ArrayList<>();
    int lastPoints = -999999999;
    int lastPlace = 0;
    int thisPlace = 0;
    for (int place = 0; place < list.size(); place++) {
      //if two players have equal points they get the same placement
      if  (lastPoints == score.getPoints().get(list.get(place).getClientID())){
        thisPlace = lastPlace;
      }
      else{
        thisPlace = lastPlace+1;
      }
      leaderlist.add(new PlayerResult(
        list.get(place).getName(),
        score.getPoints().get(list.get(place).getClientID()),
        thisPlace));
      lastPoints = score.getPoints().get(list.get(place).getClientID());
      lastPlace = thisPlace;
    }
    scoreTable.getItems().addAll(leaderlist);
  }


  /**
   * @use Create the Scene for LeaderBoard
   * @author Lennart 
   * @param event event from the current scene to build this scene on same object
   * @throws IOException error creating the scene LeaderBoard
   */
  public void create(@NotNull final ActionEvent event) throws Exception {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.close();

    Stage stage = new Stage();
    // Set scene
    var loader = SceneController.loadFXML("player/LeaderBoard.fxml");
    loader.setController(this);
    Parent root = loader.load();
    stage.setTitle("Maulwurf Company");
    stage.setResizable(false);
    stage.setScene(new Scene(root));
    stage.show();
    stage.setOnCloseRequest(ev -> logout(stage));

    // region Create button events
    // set event for backToMenu button
    var btnBack = (Button) (stage.getScene().lookup("#btnToMenu"));
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
  void backToMenu(@NotNull final ActionEvent event) throws IOException {
    new PlayerMenu().create(event);
  }
}
