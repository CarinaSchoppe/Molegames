package de.thundergames.gameplay.player.ui;

public class Utils {
    public static String getSprite(String spriteName) {
        return Utils.class.getResource("/sprites/" + spriteName).toString();
    }
}
