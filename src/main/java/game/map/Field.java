package game.map;

public class Field {
    private final int id;
    private boolean used;
    private final boolean hole;

    public Field(int id, boolean used, boolean hole) {
        this.id = id;
        this.used = used;
        this.hole = hole;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isHole() {
        return hole;
    }

    public int getId() {
        return id;
    }
}
