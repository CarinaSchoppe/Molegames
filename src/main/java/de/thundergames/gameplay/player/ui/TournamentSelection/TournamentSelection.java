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

package de.thundergames.gameplay.player.ui.TournamentSelection;

import de.thundergames.gameplay.player.networking.Client;
import de.thundergames.gameplay.player.ui.GameSelection.GameSelection;
import de.thundergames.gameplay.player.ui.SelectionShared;
import de.thundergames.networking.util.interfaceItems.NetworkGame;
import de.thundergames.playmechanics.util.Tournament;
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

public class TournamentSelection extends SelectionShared implements Initializable {


    private static Client client;
    @FXML
    private Text PlayerName;
    @FXML
    private TableView<Tournament> gameTable;
    @FXML
    private TableColumn<Tournament, Integer> tournament_Id;
    @FXML
    private TableColumn<Tournament, String> player_Count;

    private static TournamentSelection tournamentSelection;

    public static TournamentSelection getTournamentSelection(){
        return  tournamentSelection;
    }

    public void create(ActionEvent event) throws IOException {
        createScene(event,"src/main/java/de/thundergames/gameplay/player/ui/TournamentSelection/TournamentSelection.fxml");
        tournamentSelection = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Client.getClient();

        //show username at scene
        PlayerName.setText("Spieler: " + client.name);

        // set value for each row
        tournament_Id.setCellValueFactory(new PropertyValueFactory("HashtagWithTournamentID"));
        player_Count.setCellValueFactory(new PropertyValueFactory("playerCount"));

        // load data for tableview
        updateTable();
    }

    /*
    Refresh the games of tableview
     */
    public void updateTable() {
        //clear tableview and get tournaments from server and add all to table view
        gameTable.getItems().clear();
        gameTable.getItems().addAll(client.getTournaments());
    }

    @FXML
    void onBackClick(ActionEvent event) throws IOException {
        createScene(event,"src/main/java/de/thundergames/gameplay/player/ui/PlayerMenu/PlayerMenu.fxml");
    }

    @FXML
    public void onWatchTournamentClick(ActionEvent actionEvent) {
        Object selectedItem = gameTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(null, "Es wurde kein Spiel selektiert!", "Spiel beobachten", JOptionPane.OK_OPTION);
            return;
        }
        //Todo:Falls game offen dann. Ansonsten direkt ins Spiel.
    }
}
