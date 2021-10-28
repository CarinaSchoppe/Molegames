package game.util;

import game.map.Floors;
import game.map.Field;

public class Mole {

    private final Colors color;
    private final Floors floor;
    private final Field field;
    private boolean inHole = false;

    public Mole(Colors color, Floors floor, Field field) {
        this.color = color;
        this.floor = floor;
        this.field = field;
    }

    public Colors getColor() {
        return color;
    }

    public Floors getFloor() {
        return floor;
    }

    public Field getField() {
        return field;
    }

    public boolean isMoveable() {
        return false;
    }

    public boolean isInHole() {
        return inHole;
    }

    public void setInHole(boolean inHole) {
        this.inHole = inHole;
    }
}


