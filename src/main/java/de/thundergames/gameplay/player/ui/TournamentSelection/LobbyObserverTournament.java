/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 06.12.21, 14:36 by Carina latest changes made by Carina on 06.12.21, 14:36
 * All contents of "LobbyObserverTournament" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.TournamentSelection;

import de.thundergames.gameplay.player.Client;
import de.thundergames.playmechanics.util.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyObserverTournament implements Initializable {

  @FXML private static Client client;
  private static LobbyObserverTournament observer;
  @FXML private Text PlayerName;
  @FXML private Text PlayerJoined;
  @FXML private Text JoinedSuccessfully;

  private int selectedTournamentId;

  public static LobbyObserverTournament getObserver() {
    return observer;
  }

  public void create(ActionEvent event, int tournamentId) throws IOException {
    client = Client.getClient();
    selectedTournamentId = tournamentId;
    observer = this;
    createScene(event);
  }

  private void createScene(ActionEvent event) throws IOException {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    var loader = SceneController.loadFXML("player/LobbyObserverTournament.fxml");
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));

    // region Create button events
    // set event for back button
    var btnBack = (Button) (primaryStage.getScene().lookup("#backToTournamentSelection"));
    btnBack.setOnAction(
        e -> {
          try {
            onBackClick(e);
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        });
    // endregion
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    PlayerName.setText("Spieler: " + client.name);
  }

  /**
   * @author Nick
   * @use processes the click on the back button, loads previous scene GameSelection and informs
   *     server player has left via leaveGame Packet (method inspired by "onSignOutClick()" -> see
   *     GameSelection)
   */
  @FXML
  void onBackClick(ActionEvent event) throws IOException {
    client.getClientPacketHandler().leaveTournament(client, selectedTournamentId);
    new TournamentSelection().create(event);
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
   * @author Nick
   * @use Changes the opacity of a text field with the content "Ein weiterer Spieler ist
   *     beigetreten" thus making it visible for 3 seconds when another player has joined
   *     respectively when the client has received playerJoined packet.
   */
  public void showNewPlayer() {
    PlayerJoined.setOpacity(1.0);
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    PlayerJoined.setOpacity(0.0);
  }

  /**
   * @author Nick
   * @use Changes the opacity of a text field with the content "Beitritt zum Turnier war
   *     erfolgreich! Bitte warten." thus making it visible for 5 seconds.
   */
  public void showJoiningSuccessfully() {
    JoinedSuccessfully.setOpacity(1.0);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    JoinedSuccessfully.setOpacity(0.0);
  }

  /**
   * @author Nick
   * @use Create scene and spectate the game of tournament
   */
  public void spectateGame() {
    var currentGameState = client.getGameState();
    // TODO:Create scene for game
  }
}
