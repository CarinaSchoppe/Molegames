package de.thundergames.gameplay.ausrichter.ui;

import de.thundergames.MoleGames;
import de.thundergames.gameplay.ausrichter.AusrichterClient;
import de.thundergames.networking.server.Server;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import java.net.URL;
import java.util.ResourceBundle;

public class Lobby extends Application implements Initializable {

  private static Lobby GUI;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button games;

  @FXML
  private Button tournaments;


  public static void create(@NotNull final Server server) {
    MoleGames.getMoleGames().setAusrichterClient(new AusrichterClient(server));
    new Thread(Application::launch).start();
  }

  public static Lobby getGUI() {
    return GUI;
  }


  /**
   * @param stage current stage
   * @use Is called when the close button is clicked. Logout user.
   * @author Marc
   */
  private void logout(Stage stage) {
    stage.close();
    System.exit(0);
  }


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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    GUI = this;
    initialize();
  }

  public void start(@NotNull final Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(getClass().getResource("/ausrichter/style/Lobby.fxml"));
    loader.setController(this);
    var root = (Parent) loader.load();
    primaryStage.setTitle("Lobby!");
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    initialize();
    primaryStage.show();
  }
}
