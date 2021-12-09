package de.thundergames.playmechanics.Board;

public class Utils {
    public static String getSprite(String spriteName) {
        return Utils.class.getResource("/sprites/" + spriteName).toString();
    }
}
