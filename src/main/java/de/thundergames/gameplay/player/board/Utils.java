/*
 * Copyright Notice for SwtPra10
 * Copyright (c) at ThunderGames | SwtPra10 2022
 * File created on 09.01.22, 12:04 by Carina Latest changes made by Carina on 09.01.22, 12:04 All contents of "Utils" are protected by copyright. The copyright law, unless expressly indicated otherwise, is
 * at ThunderGames | SwtPra10. All rights reserved
 * Any type of duplication, distribution, rental, sale, award,
 * Public accessibility or other use
 * requires the express written consent of ThunderGames | SwtPra10.
 */s Utils {
  public static String getSprite(@NotNull final String spriteName) {
    return Objects.requireNonNull(Utils.class.getResource("/sprites/" + spriteName)).toString();
  }

  public static String getRandomHSLAColor() {
    return "hsla(" + Math.random() * 360 + ", 100%, 50%, 1)";
  }
}
