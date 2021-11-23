/*
 * Copyright Notice for Swtpra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 23.11.21, 18:38 by nickl
 * Latest changes made by nickl on 23.11.21, 18:38
 * All contents of "LobbyObserver" are protected by copyright.
 * The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class LobbyObserver extends Application {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView participant;

    @FXML
    private ListView observer;

    @FXML
    private Button refresh;

    @FXML
    private Button back;

    @FXML
    private Text showtimer;


    @Override
    public void start(@NotNull Stage stage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LobbyObserver.fxml")));
        Scene scene = new Scene(root, 900, 856);
        stage.setTitle("LobbyObserver");
        stage.setScene(scene);
        stage.show();


        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

    }
/*
    public void timer (){
        Timer timer = new timer();
        return timer.scheduleAtFixedRate(new TimerTask() {
            private int Seconds = 5;

            @Override
            public void run() {
                showtimer.setTextContent(String.valueOf(Seconds));
                Seconds--;
                if (Seconds == 0) {
                    timer.cancel();
                }
            }
        }
    }
 */
}

