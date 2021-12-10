package de.thundergames.playmechanics.board;

import org.jetbrains.annotations.NotNull;

public class Utils {
  public static String getSprite(@NotNull final String spriteName) {
    return Utils.class.getResource("/sprites/" + spriteName).toString();
  }
}
