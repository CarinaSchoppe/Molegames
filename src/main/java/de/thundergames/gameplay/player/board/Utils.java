/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 17.01.22, 19:10 by Carina Latest changes made by Carina on 17.01.22, 19:10 All contents of "Utils" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */

package de.thundergames.gameplay.player.board;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class Utils {
  public static String getSprite(@NotNull final String spriteName) {
    return Objects.requireNonNull(Utils.class.getResource("/sprites/" + spriteName)).toString();
  }

  // Code originated from:
  // https://mika-s.github.io/javascript/colors/hsl/2017/12/05/generating-random-colors-in-javascript.html
  // This method was slightly modified.
  public static ArrayList<String> getRandomHSLAColor(int amount) {
    ArrayList<String> colors = new ArrayList<>(amount);
    var huedelta = 360 / amount;
    for (var i = 0; i < amount; i++) {
      var hue = i * huedelta;
      colors.add("hsla(" + hue + ", 100%, 50%, 1)");
    }
    return colors;
  }
}
