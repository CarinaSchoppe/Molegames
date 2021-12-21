/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2021
 * File created on 21.12.21, 13:57 by Carina Latest changes made by Carina on 21.12.21, 13:55 All contents of "SceneController" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.util;

import javafx.fxml.FXMLLoader;

public class SceneController {

  public static FXMLLoader loadFXML(String path) {
    return new FXMLLoader(SceneController.class.getResource(path));
  }
}
