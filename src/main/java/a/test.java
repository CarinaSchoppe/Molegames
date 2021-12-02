/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 02.12.21, 17:19 by Carina latest changes made by Carina on 02.12.21, 17:01
 * All contents of "test" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package a;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.File;

public class test extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    var loader = new FXMLLoader(new File("src/main/java/a/test.fxml").toURI().toURL()){

    }
  }
}
