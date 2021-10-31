package game.util;

public enum Punishments {


    KICK(0),
    POINTS(1);


    private final int id;
    private Punishments punishment;


    Punishments(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPunishment(Punishments punishment) {
        this.punishment = punishment;
    }

    public Punishments getPunishment() {
        return punishment;
    }
}
