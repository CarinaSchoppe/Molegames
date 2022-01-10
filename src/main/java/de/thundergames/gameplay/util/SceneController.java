/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 21:21 by Carina Latest changes made by Carina on 09.01.22, 21:18 All contents of "SceneController" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.util;

import javafx.fxml.FXMLLoader;
import org.jetbrains.annotations.NotNull;

public class SceneController {

  public static FXMLLoader loadFXML(@NotNull final String path) {
    return new FXMLLoader(SceneController.class.getResource(path));
  }
}
