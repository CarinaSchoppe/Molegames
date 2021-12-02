/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.11.21, 22:01 by MarcW
 * Latest changes made by MarcW on 21.11.21, 22:01
 * All contents of "PlayerWorkspace" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui.PlayerMenu;

import de.thundergames.gameplay.player.networking.Client;
import de.thundergames.gameplay.player.ui.GameSelection.GameSelection;
import de.thundergames.gameplay.player.ui.TournamentSelection.TournamentSelection;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.playmechanics.game.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerMenu  implements Initializable {


    private static Client client;
    @FXML
    private Text PlayerName;

    public void create(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File("src/main/java/de/thundergames/gameplay/player/ui/PlayerMenu/PlayerMenu.fxml")
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setTitle("Maulwurf Company");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Client.getClient();

        //show username at scene
        PlayerName.setText("Spieler: " + client.name);
    }

    @FXML
    void onSignOutClick(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL location =
                new File("src/main/java/de/thundergames/gameplay/player/ui/LoginScreen.fxml")
                        .toURI()
                        .toURL();
        Parent root = FXMLLoader.load(location);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        //Todo: client.disconnect();
    }

    public void onGameClick(ActionEvent event) throws IOException {
        new GameSelection().create(event);
    }

    public void onTournamentClick(ActionEvent event) throws IOException {
        new TournamentSelection().create(event);
    }
}
