package de.thundergames.gameplay.player.ui.score;

import de.thundergames.filehandling.Score;
import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.player.ui.PlayerMenu;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.networking.util.interfaceitems.NetworkPlayer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class LeaderBoard extends Application implements Initializable {

  private static Client client;

  @FXML
  private TableView<PlayerResult> scoreTable;

  @FXML
  private TableColumn<PlayerResult, Integer> placement;

  @FXML
  private TableColumn<PlayerResult, String> name;

  @FXML
  private TableColumn<PlayerResult, Integer> score;

  /**
   * @author Carina, Lennart
   * @use launches the Scene
   */
  public static void create() {
    launch();
  }

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
    var score = client.getGameState().getScore();
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
    var leaderlist = new ArrayList<PlayerResult>();
    var lastPoints = -999999999;
    var lastPlace = 0;
    var thisPlace = 0;
    for (var place = 0; place < list.size(); place++) {
      //if two players have equal points they get the same placement
      if (lastPoints == score.getPoints().get(list.get(place).getClientID())) {
        thisPlace = lastPlace;
      } else {
        thisPlace = lastPlace + 1;
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
   * Is called when the close button is clicked. Logout user.
   *
   * @param stage current stage
   */
  private void logout(@NotNull final Stage stage) {
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

  /**
   * @param primaryStage stage that will be opend
   * @throws IOException error creating the scene LeaderBoard
   * @use Create the Scene for LeaderBoard
   * @author Lennart, Carina
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    var loader = SceneController.loadFXML("/player/style/LeaderBoard.fxml");
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    // region Create button events
    // set event for backToMenu button
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
}
