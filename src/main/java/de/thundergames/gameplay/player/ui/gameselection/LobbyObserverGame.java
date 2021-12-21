/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 15:22 by Carina Latest changes made by Carina on 21.12.21, 15:21 All contents of "LobbyObserverGame" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.gameselection;

import de.thundergames.gameplay.player.Client;
import de.thundergames.gameplay.util.SceneController;
import de.thundergames.playmechanics.board.TestWindow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyObserverGame implements Initializable {

  @FXML
  private static Client CLIENT;
  private static LobbyObserverGame OBSERVER;
  @FXML
  private Text PlayerName;
  @FXML
  private Text PlayerJoined;
  @FXML
  private Text JoinedSuccessfully;

  private Stage primaryStage;

  public static LobbyObserverGame getObserver() {
    return OBSERVER;
  }

  public void create(Stage event) throws IOException {
    CLIENT = Client.getClientInstance();
    OBSERVER = this;
    createScene(event);
  }

  private void createScene(Stage primaryStage) throws IOException {
    this.primaryStage = primaryStage;
    var loader = SceneController.loadFXML("/player/style/LobbyObserverGame.fxml");
    loader.setController(this);
    Parent root = loader.load();
    primaryStage.setTitle("Maulwurf Company");
    primaryStage.setResizable(false);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    primaryStage.setOnCloseRequest(ev -> logout(primaryStage));
    // region Create button events
    // set event for back button
    var btnBack = (Button) (primaryStage.getScene().lookup("#backToGameSelection"));
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
    PlayerName.setText("Spieler: " + CLIENT.name);
  }

  /**
   * @author Nick
   * @use processes the click on the back button, loads previous scene GameSelection and informs
   * server player has left via leaveGame Packet (method inspired by "onSignOutClick()" -> see
   * GameSelection)
   */
  @FXML
  void onBackClick(ActionEvent event) throws IOException {
    CLIENT.getClientPacketHandler().leaveGamePacket();
    new GameSelection().create(event);
  }

  /**
   * Is called when the close button is clicked. Logout user.
   *
   * @param stage current stage
   */
  private void logout(Stage stage) {
    CLIENT.getClientPacketHandler().logoutPacket();
    CLIENT.getClientPacketHandler().leaveGamePacket();
    stage.close();
  }

  /**
   * @author Nick
   * @use Changes the opacity of a text field with the content "Ein weiterer Spieler ist
   * beigetreten" thus making it visible for 3 seconds when another player has joined
   * respectively when the client has received playerJoined packet.
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
   * @use Changes the opacity of a text field with the content "Beitritt zum Spiel war erfolgreich!
   * Bitte warten." thus making it visible for 5 seconds when the client has received
   * AssignToGame packet.
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
   * @use Create scene and spectate the game
   */
  public void spectateGame() {
    Platform.runLater(
      () -> {
        new TestWindow().start(primaryStage);
      });
  }
}
