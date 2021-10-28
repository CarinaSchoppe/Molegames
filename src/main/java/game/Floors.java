package game;

import game.map.Map;

public class Floors extends Map {

    private final int floorID;


    public Floors(int floorID) {
        this.floorID = floorID;
    }

    public int getFloorID() {
        return floorID;
    }
}
